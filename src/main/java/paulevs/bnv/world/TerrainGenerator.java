package paulevs.bnv.world;

import net.minecraft.util.Mth;
import ru.bclib.noise.OpenSimplexNoise;
import ru.bclib.util.MHelper;

import java.util.Random;

public class TerrainGenerator {
	private static OpenSimplexNoise noiseH;
	private static OpenSimplexNoise noiseV;
	private static long seed;
	private static short minY;
	private static short maxY;
	private static short middle;
	
	public static void init(long seed, int minY, int maxY) {
		if (TerrainGenerator.seed == seed && noiseH != null) {
			return;
		}
		
		TerrainGenerator.seed = seed;
		TerrainGenerator.minY = (short) minY;
		TerrainGenerator.maxY = (short) maxY;
		TerrainGenerator.middle = (short) ((maxY - minY) / 2);
		
		Random random = new Random(seed);
		noiseH = new OpenSimplexNoise(random.nextInt());
		noiseV = new OpenSimplexNoise(random.nextInt());
	}
	
	// BNB generator port, scaled. Will be replaced
	public static void fillData(double[] data, int ix, int iz, int sizeY) {
		double px = ix * 0.5;
		double pz = iz * 0.5;
		
		float noiseHor1 = (float) noiseV.eval(px * 0.01, pz * 0.01);
		float noiseHor2 = (float) noiseH.eval(px * 0.03, pz * 0.03);
		float noiseHor3 = (float) noiseV.eval(px * 0.001, pz * 0.010);
		float ceiling = (float) noiseH.eval(px * 0.02, pz * 0.02) * 12 + (float) noiseH.eval(px * 0.1, pz * 0.1) * 5;
		
		final short last = (short) (data.length - 1);
		
		data[0] = 2;
		data[last] = 2;
		
		for (short index = 1; index < last; index++) {
			float py = (index * sizeY + minY) * 0.5F + 32;
			
			// Big pillars and ceiling/floor
			float gradient = 55 - Mth.abs(py - 63.5F);
			float pillars = (float) noiseV.eval(px * 0.01, py * 0.01, pz * 0.01) * 80 + 40F;
			pillars += (float) noiseV.eval(px * 0.03, py * 0.03, pz * 0.03) * 10;
			float val = smoothUnion(gradient, pillars, 40F);
			
			// Tubes
			float noise = Mth.abs((float) noiseH.eval(px * 0.02, py * 0.02, pz * 0.02));
			float vert = (float) Math.sin((py + noiseHor1 * 20) * 0.1F) * 0.9F;
			noise += Mth.clamp((pillars - 40), 0, 2) + vert * vert;
			val = smoothUnion(val, (noise - 0.2F) * 40, 20F);
			
			// Plateau
			float height = noiseHor1;
			float variation = noiseHor2 * 5;
			variation += noiseHor3 * 10 + 40;
			height = (float) Math.atan(height * 30) / 1.5374F * variation;
			gradient = (py - height);
			val = smoothUnion(val, gradient, 100F);
			
			// Shore
			height = (float) Math.atan(noiseHor1 + 0.2) / 1.5374F * variation;
			gradient = (py - height + 10);
			val = smoothUnion(val, gradient, 100F);
			
			// Ceiling
			val = MHelper.min(val, ceiling + 120 - py);
			
			// Some variations
			val += noiseH.eval(px * 0.03, pz * 0.03) * 4;
			val += noiseV.eval(px * 0.1, pz * 0.1) * 2;
			
			data[index] = -val;
		}
	}
	
	private static float smoothUnion(float a, float b, float delta) {
		float h = Mth.clamp(0.5F + 0.5F * (b - a) / delta, 0, 1);
		return Mth.lerp(h, b, a) - delta * h * (1 - h);
	}
}
