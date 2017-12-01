import static org.lwjgl.glfw.GLFW.GLFW_KEY_LAST;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;

public class Input {
	private long window;
	private boolean keys[];

	public Input(long window) {
		this.window = window;
		this.keys = new boolean[GLFW_KEY_LAST];

		for (int i=0; i<this.keys.length; i++) {
			this.keys[i] = false;
		}
	}

	public boolean isKeyDown(int key) {
		return (glfwGetKey(this.window, key) == 1);
	}

	public boolean isKeyPressed(int key) {
		return (isKeyDown(key) && !this.keys[key]);
	}

	public boolean isKeyReleased(int key) {
		return (!isKeyDown(key) && this.keys[key]);
	}

	public boolean isMouseButtonDown(int button) {
		return glfwGetMouseButton(this.window, button) == 1;
	}

	public void update() {
		for (int i=32; i<this.keys.length; i++) {
			this.keys[i] = isKeyDown(i);
		}
	}
}
