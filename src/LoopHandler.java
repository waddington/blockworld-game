import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class LoopHandler {
	private long window;

	LoopHandler(long window) {
		this.window = window;
	}

	void go() {

		GL.createCapabilities();
		glClearColor(0, 0, 0, 0);
		glEnable(GL_TEXTURE_2D);

		float[] vertices = new float[] {-0.5f, 0.5f, 0, 0.5f, 0.5f, 0, 0.5f, -0.5f, 0, -0.5f, -0.5f, 0};
		float[] texture = new float[] {0, 0, 1, 0, 1, 1, 0, 1,};
		int[] indices = new int[] {0, 1, 2, 2, 3, 0};

		Model model = new Model(vertices, texture, indices);
		Shader shader = new Shader("shader");

//		Texture textureTexture = new Texture("./res/smiley.png");

		while (!glfwWindowShouldClose(this.window)) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

//			textureTexture.bind();
			shader.bind();
			model.render();


			glfwSwapBuffers(this.window);
			glfwPollEvents();
		}
	}
}
