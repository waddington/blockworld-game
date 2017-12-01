import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class LoopHandler {
	private Window window;

	LoopHandler(Window window) {
		this.window = window;
	}

	void go() {

		GL.createCapabilities();
		glClearColor(0, 0, 0, 0);
		glEnable(GL_TEXTURE_2D);

		Camera camera = new Camera(this.window.getWidth(), this.window.getHeight());

		float[] vertices = new float[] {-0.5f, 0.5f, 0, 0.5f, 0.5f, 0, 0.5f, -0.5f, 0, -0.5f, -0.5f, 0};
		float[] texture = new float[] {0, 0, 1, 0, 1, 1, 0, 1,};
		int[] indices = new int[] {0, 1, 2, 2, 3, 0};

		Model model = new Model(vertices, texture, indices);
		Shader shader = new Shader("shader");

		Texture textureTexture = new Texture("./res/smiley.png");
		Matrix4f projection = new Matrix4f().ortho2D(-640/2, 640/2, -480/2, 480/2);
		Matrix4f scale = new Matrix4f().translate(new Vector3f(100, 0, 0)).scale(320);
		Matrix4f target = new Matrix4f();

		projection.mul(scale, target);
		camera.setPosition(new Vector3f(-100, 0, 0));

		double frameCap = 1.0 / 60.0;
		double time = Timer.getTime();
		double unprocessed = 0;
		double frameTime = 0;
		int frames = 0;

		while (!this.window.shouldClose()) {
			boolean canRender = false;

			double time2 = Timer.getTime();
			double passed = time2 - time;
			unprocessed += passed;
			frameTime += passed;

			time = time2;

			// Update code
			while (unprocessed >= frameCap) {
				unprocessed -= frameCap;
				canRender = true;

				target = scale;

				if (this.window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
					glfwSetWindowShouldClose(this.window.getWindow(), true);
				}

				this.window.update();

				if (frameTime >= 1.0) {
					frameTime = 0;
					System.out.println("FPS: " + frames);
					frames = 0;
				}
			}

			// Render code
			if (canRender) {
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

				shader.bind();
				shader.setUniform("sampler", 0);
				shader.setUniform("projection", camera.getProjection().mul(target));
				textureTexture.bind(0);
				model.render();

				this.window.swapBuffers();
				frames++;
			}
		}
	}
}
