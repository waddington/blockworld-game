package game;

import entity.Entity;
import entity.Player;
import entity.Transform;
import io.Timer;
import io.Window;
import org.lwjgl.opengl.GL;
import render.Camera;
import render.Shader;
import world.TileRenderer;
import world.World;

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
		TileRenderer tileRenderer = new TileRenderer();
		Entity.initAsset();
		World world = new World("test_level");
		Player player = new Player(new Transform());

		double frameCap = 1.0 / 30.0;
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

				if (this.window.getInput().isKeyPressed(GLFW_KEY_ESCAPE)) {
					glfwSetWindowShouldClose(this.window.getWindow(), true);
				}

				player.update((float) frameCap, this.window, camera, world);
				world.correctCamera(camera, this.window);
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

				world.render(tileRenderer, shader, camera, this.window);
				player.render(shader, camera, world);

				this.window.swapBuffers();
				frames++;
			}
		}
	}
}
