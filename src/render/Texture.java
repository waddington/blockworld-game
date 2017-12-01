package render;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Texture {
	private int id;
	private int width;
	private int height;

	public Texture(String filename) {
		BufferedImage bufferedImage;

		try {
			bufferedImage = ImageIO.read(new File(filename));

			this.width = bufferedImage.getWidth();
			this.height = bufferedImage.getHeight();

			int[] pixelsRaw = new int[this.width * this.height * 4];
			pixelsRaw = bufferedImage.getRGB(0,0, this.width, this.height, null, 0, this.width);

			ByteBuffer pixels = BufferUtils.createByteBuffer(this.width * this.height * 4);

			for (int i=0; i<this.width; i++) {
				for (int j=0; j<this.height; j++) {
					int pixel = pixelsRaw[i*this.width + j];
					// rgba
					pixels.put((byte) ((pixel >> 16) & 0xFF));
					pixels.put((byte) ((pixel >> 8) & 0xFF));
					pixels.put((byte) ((pixel) & 0xFF));
					pixels.put((byte) ((pixel >> 24) & 0xFF));
				}
			}

			pixels.flip();

			this.id = glGenTextures();

			glBindTexture(GL_TEXTURE_2D, this.id);

			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void bind(int sampler) {

		if (sampler >= 0 && sampler <= 31) {
			glActiveTexture(GL_TEXTURE0 + sampler);
			glBindTexture(GL_TEXTURE_2D, this.id);
		}
	}
}
