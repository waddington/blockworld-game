import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class Model {
	private int drawCount;
	private int vertexId;
	private int textureId;
	private int indicesId;

	public Model(float[] vertices, float[] textureCoords, int[] indices) {
		this.drawCount = indices.length;

		this.vertexId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, this.vertexId);
		glBufferData(GL_ARRAY_BUFFER, createBuffer(vertices), GL_STATIC_DRAW);

		this.textureId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, this.textureId);
		glBufferData(GL_ARRAY_BUFFER, createBuffer(textureCoords), GL_STATIC_DRAW);

		this.indicesId = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.indicesId);
		IntBuffer intBuffer = BufferUtils.createIntBuffer(indices.length);
		intBuffer.put(indices);
		intBuffer.flip();
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, intBuffer, GL_STATIC_DRAW);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0); // Unbind so nothing can effect it
	}

	private FloatBuffer createBuffer(float[] data) {
		FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(data.length);
		floatBuffer.put(data);
		floatBuffer.flip();

		return floatBuffer;
	}

	public void render() {
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		glBindBuffer(GL_ARRAY_BUFFER, this.vertexId);

		glVertexPointer(3, GL_FLOAT,0,0);

		glBindBuffer(GL_ARRAY_BUFFER, this.textureId);
		glTexCoordPointer(2, GL_FLOAT, 0, 0);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.indicesId);

		glDrawElements(GL_TRIANGLES, this.drawCount, GL_UNSIGNED_INT, 0);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);
	}
}
