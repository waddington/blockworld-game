package collision;

import org.joml.Vector2f;

public class AABB {
	private Vector2f center;
	private Vector2f halfExtent;

	public AABB(Vector2f center, Vector2f halfExtent) {
		this.center = center;
		this.halfExtent = halfExtent;
	}

	public Vector2f getCenter() {
		return this.center;
	}

	public Vector2f getHalfExtent() {
		return this.halfExtent;
	}

	public Collision getCollision(AABB box2) {
		Vector2f distance = box2.center.sub(this.center, new Vector2f());
		distance.x = Math.abs(distance.x);
		distance.y = Math.abs(distance.y);

		distance.sub(this.halfExtent.add(box2.halfExtent, new Vector2f()));

		return new Collision(distance, (distance.x < 0 && distance.y < 0));
	}

	public void correctPosition(AABB box2, Collision data) {
		Vector2f correction = box2.center.sub(this.center, new Vector2f());

		if (data.distance.x > data.distance.y) {
			if (correction.x > 0) {
				this.center.add(data.distance.x, 0);
			} else {
				this.center.add(-data.distance.x, 0);
			}
		} else {
			if (correction.y > 0) {
				this.center.add(0, data.distance.y);
			} else {
				this.center.add(0, -data.distance.y);
			}
		}
	}
}
