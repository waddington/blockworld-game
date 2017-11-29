import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

class Main {
	private Window window;
	private LoopHandler loopHandler;

	private void run() {
		init();
		loop();

		glfwFreeCallbacks(this.window.getWindow());
		glfwDestroyWindow(this.window.getWindow());

		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void init() {
		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW.");
		}

		this.window = new Window();
		this.window.createWindow("Hello World! v2");

		this.loopHandler = new LoopHandler(this.window);
	}

	private void loop() {
		this.loopHandler.go();
	}

	public static void main(String[] args) {
		new Main().run();
	}
}
