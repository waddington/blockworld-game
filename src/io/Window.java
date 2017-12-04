package io;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
	private long window;
	private int width, height;
	private boolean fullscreen;
	private long primaryMonitor;
	private GLFWVidMode vidMode;
	private Input input;

	public Window() {
		setSize(640,480);
		setFullscreen(false);
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

	public boolean isFullscreen() {
		return this.fullscreen;
	}

	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}

	public Input getInput() {
		return this.input;
	}

	public void setInput(Input input) {
		this.input = input;
	}

	public void createWindow(String title) {
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

		this.primaryMonitor = glfwGetPrimaryMonitor();
		this.vidMode = glfwGetVideoMode(this.primaryMonitor);

		this.window = glfwCreateWindow(
				this.width,
				this.height,
				title,
				this.fullscreen ? glfwGetPrimaryMonitor() : NULL,
				NULL
		);

		if (this.window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window.");
		}

		if (!isFullscreen()) {
			try (MemoryStack stack = stackPush()) {
				IntBuffer pWidth = stack.mallocInt(1);
				IntBuffer pHeight = stack.mallocInt(1);

				glfwGetWindowSize(this.window, pWidth, pHeight);

				glfwSetWindowPos(
						this.window,
						(this.vidMode.width() - pWidth.get(0)) / 2,
						(this.vidMode.height() - pHeight.get(0)) / 2);
			}
		}

		this.input = new Input(this.window);

		glfwMakeContextCurrent(this.window);
		glfwSwapInterval(1);

		glfwShowWindow(this.window);
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(this.window);
	}

	public void swapBuffers() {
		glfwSwapBuffers(this.window);
	}

	public static void setCallbacks() {
		glfwSetErrorCallback(new GLFWErrorCallback() {
			@Override
			public void invoke(int error, long description) {
				throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
			}
		});
	}

	public void update() {
		this.input.update();
		glfwPollEvents();
	}
}
