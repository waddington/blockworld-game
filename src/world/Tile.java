package world;

public class Tile {
	public static Tile tiles[] = new Tile[16];
	public static int numberOfTiles = 0;
	public static final Tile testTile = new Tile("grass");
	public static final Tile testTile2 = new Tile("checker");

	private byte id;
	private String texture;

	public Tile(String texture) {
		this.id = (byte) numberOfTiles++;
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
