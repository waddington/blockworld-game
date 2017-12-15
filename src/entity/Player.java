package entity;

import io.Window;
import org.joml.Vector2f;
import render.Animation;
import render.Camera;
import world.World;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends Entity {
	private int moveMultiplier = 15;

	public Player(Transform transform) {
		super(new Animation(5, 15, "an"), transform);
	}

	@Override
	public void update(float delta, Window window, Camera camera, World world) {
		Vector2f movement = new Vector2f();

		if (window.getInput().isKeyDown(GLFW_KEY_A)) {
			movement.add(-this.moveMultiplier *delta, 0);
		} else if (window.getInput().isKeyDown(GLFW_KEY_D)) {
			movement.add(this.moveMultiplier *delta, 0);
		}

		if (window.getInput().isKeyDown(GLFW_KEY_W)) {
			movement.add(0, this.moveMultiplier *delta);
		} else if (window.getInput().isKeyDown(GLFW_KEY_S)) {
			movement.add(0, -this.moveMultiplier *delta);
		}

		move(movement);
		super.update(delta, window, camera, world);
	}
}
