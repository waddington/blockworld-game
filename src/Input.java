import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;

public class Input {
	private long window;

	public Input(long window) {
		this.window = window;
	}

	public boolean isKeyDown(int key) {
		return glfwGetKey(this.window, key) == 1;
	}

	public boolean isMouseButtonDown(int button) {
		return glfwGetMouseButton(this.window, button) == 1;
	}
}
