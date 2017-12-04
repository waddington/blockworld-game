package game;

import io.Timer;
import io.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import render.Camera;
import render.Shader;
import render.Texture;
import world.TileRenderer;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
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
		Shader shader = new Shader("shader");
		Texture textureTexture = new Texture("./res/test.png");
		TileRenderer tileRenderer = new TileRenderer();

		Matrix4f projection = new Matrix4f().ortho2D(-640/2, 640/2, -480/2, 480/2);
		Matrix4f scale = new Matrix4f().translate(new Vector3f(0, 0, 0)).scale(16);
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

				for (int i=0; i<8; i++) {
					for (int j=0; j<4; j++) {
						tileRenderer.renderTile((byte) 0, i, j, shader, scale, camera);
					}
				}

				this.window.swapBuffers();
				frames++;
			}
		}
	}
}
