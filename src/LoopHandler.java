public class LoopHandler {
	private long window;

	LoopHandler(long window) {
		this.window = window;
	}

	public void go() {
		drawModel();
	}

	private void drawModel() {
		float[] vertices = new float[] {
				-0.5f, 0.5f, 0,
				0.5f, 0.5f, 0,
				0.5f, -0.5f, 0,
				-0.5f, -0.5f, 0
		};

		float[] texture = new float[] {
				0, 0,
				1, 0,
				1, 1,
				0, 1,
		};

		int[] indices = new int[] {
				0,1,2,
				2,3,0
		};

		Model model = new Model(vertices, texture, indices);

		Texture textureTexture = new Texture("./res/smiley.png");
		textureTexture.bind();

		model.render();

	}
}
