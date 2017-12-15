package world;

public class Tile {
	public static Tile tiles[] = new Tile[255];
	public static int numberOfTiles = 0;
	public static final Tile testTile = new Tile("grass");
	public static final Tile testTile2 = new Tile("checker").setSolid();

	private byte id;
	private String texture;
	private boolean isSolid;

	public Tile(String texture) {
		this.id = (byte) numberOfTiles++;
		this.texture = texture;
		this.isSolid = false;

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

	public Tile setSolid() {
		this.isSolid = true;
		return this;
	}

	public boolean isSolid() {
		return this.isSolid;
	}
}
