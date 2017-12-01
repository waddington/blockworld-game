package world;

public class Tile {
	public static Tile tiles[] = new Tile[16];
	public static final Tile testTile = new Tile((byte) 0, "test");

	private byte id;
	private String texture;

	public Tile(byte id, String texture) {
		this.id = id;
		this.texture = texture;

		if (Tile.tiles[id] != null) {
			throw new IllegalStateException("Tiles at: [" + id + "] is already being used.");
		}

		Tile.tiles[id] = this;
	}

	public byte getId() {
		return this.id;
	}

	public String getTexture() {
		return texture;
	}
}
