import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
	private int program;
	private int vs;
	private int fs;

	public Shader(String filename) {
		this.program = glCreateProgram();

		this.vs = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(this.vs, readFile(filename + ".vs"));
		glCompileShader(this.vs);

		if (glGetShaderi(this.vs, GL_COMPILE_STATUS) != 1) {
			System.err.println(glGetShaderInfoLog(this.vs));
			System.exit(1);
		}

		this.fs = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(this.fs, readFile(filename + ".fs"));
		glCompileShader(this.fs);

		if (glGetShaderi(this.fs, GL_COMPILE_STATUS) != 1) {
			System.err.println(glGetShaderInfoLog(this.fs));
			System.exit(1);
		}

		glAttachShader(this.program, this.vs);
		glAttachShader(this.program, this.fs);

		glBindAttribLocation(this.program, 0, "vertices");
		glBindAttribLocation(this.program, 1, "textures");

		glLinkProgram(this.program);
		if (glGetProgrami(this.program, GL_LINK_STATUS) != 1) {
			System.err.println(glGetProgramInfoLog(this.program));
			System.exit(1);
		}

		glValidateProgram(this.program);
		if (glGetProgrami(this.program, GL_VALIDATE_STATUS) != 1) {
			System.err.println(glGetProgramInfoLog(this.program));
			System.exit(1);
		}
	}

	private String readFile(String filename) {
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader;

		try {
			bufferedReader = new BufferedReader(new FileReader(new File("./shaders/" + filename)));
			String line;

			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append("\n");
			}

			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stringBuilder.toString();
	}

	public void bind() {
		glUseProgram(this.program);
	}

	public void setUniform(String name, int value) {
		int location = glGetUniformLocation(this.program, name);

		if (location != -1) {
			glUniform1i(location, value);
		}
	}

	public void setUniform(String name, Matrix4f value) {
		int location = glGetUniformLocation(this.program, name);
		FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);
		value.get(floatBuffer);

		if (location != -1) {
			glUniformMatrix4fv(location, false, floatBuffer);
		}
	}


}
