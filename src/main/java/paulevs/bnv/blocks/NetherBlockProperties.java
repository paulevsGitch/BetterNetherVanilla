package paulevs.bnv.blocks;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class NetherBlockProperties {
	public static final EnumProperty<TripplePlant> TRIPPLE_PLANT = EnumProperty.create("shape", TripplePlant.class);
	public static final BooleanProperty[] DIRECTION_BOOLEAN = new BooleanProperty[] {
		BooleanProperty.create("north"),
		BooleanProperty.create("east"),
		BooleanProperty.create("south"),
		BooleanProperty.create("west")
	};
	
	public enum TripplePlant implements StringRepresentable {
		SMALL("small"),
		BOTTOM("bottom"),
		TOP("top");
		
		private final String name;
		
		TripplePlant(String name) {
			this.name = name;
		}
		
		@Override
		public String getSerializedName() {
			return name;
		}
	}
}
