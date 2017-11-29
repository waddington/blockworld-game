import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
	private long window;
	private int width, height;
	private long primaryMonitor;
	private GLFWVidMode vidMode;

	Window() {
		setSize(640,480);
	}

	public long getWindow() {
		return this.window;
	}

	public void setWindow(long window) {
		this.window = window;
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void createWindow(String title) {
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

		this.primaryMonitor = glfwGetPrimaryMonitor();
		this.vidMode = glfwGetVideoMode(this.primaryMonitor);

		this.window = glfwCreateWindow(this.width, this.height, title, NULL, NULL);

		if (this.window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window.");
		}

		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			glfwGetWindowSize(this.window, pWidth, pHeight);

			glfwSetWindowPos(
					this.window,
					(this.vidMode.width() - pWidth.get(0)) / 2,
					(this.vidMode.height() - pHeight.get(0)) / 2);
		}

		glfwSetKeyCallback(this.window, this::handleKeyPresses);

		glfwMakeContextCurrent(this.window);
		glfwSwapInterval(1);

		glfwShowWindow(this.window);
	}

	private void handleKeyPresses(long window, int key, int scanCode, int action, int mods) {
		// Exit on esc key release
		if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
			glfwSetWindowShouldClose(window, true);
		}
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(this.window);
	}

	public void swapBuffers() {
		glfwSwapBuffers(this.window);
	}
}
