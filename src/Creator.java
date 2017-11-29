import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

class Creator {
	private boolean isFullScreen = false;
	private long window;
	private long primaryMonitor;
	private GLFWVidMode vidMode;
	private LoopHandler loopHandler;

	Creator() {
		run();
	}

	private void run() {
		init();
		loop();

		glfwFreeCallbacks(this.window);
		glfwDestroyWindow(this.window);

		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init() {
		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW.");
		}

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

		this.primaryMonitor = glfwGetPrimaryMonitor();
		this.vidMode = glfwGetVideoMode(this.primaryMonitor);

		if (this.isFullScreen) {
			this.window = glfwCreateWindow(this.vidMode.width(), this.vidMode.height(), "Hello World!", this.primaryMonitor, NULL);
		} else {
			this.window = glfwCreateWindow(800, 600, "Hello World!", NULL, NULL);
		}

		this.loopHandler = new LoopHandler(this.window);

		if (this.window == NULL) {
			throw new RuntimeException("Failed to create the GLFW window.");
		}

		glfwSetKeyCallback(this.window, this::handleKeyPresses);

		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			glfwGetWindowSize(this.window, pWidth, pHeight);
			glfwSetWindowPos(this.window, (this.vidMode.width() - pWidth.get(0)) / 2, (this.vidMode.height() - pHeight.get(0)) / 2);
		}

		glfwMakeContextCurrent(this.window);
		glfwSwapInterval(1);

		glfwShowWindow(this.window);
	}

	private void loop() {
		GL.createCapabilities();
		glClearColor(0,0,0,0);
		glEnable(GL_TEXTURE_2D);

		while (!glfwWindowShouldClose(this.window)) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			this.loopHandler.go();

			glfwSwapBuffers(this.window);
			glfwPollEvents();
		}
	}

	private void handleKeyPresses(long window, int key, int scanCode, int action, int mods) {
		// Exit on esc key release
		if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
			glfwSetWindowShouldClose(window, true);
		}
	}
}
