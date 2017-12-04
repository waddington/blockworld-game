package render;

import io.Timer;

public class Animation {
	private Texture[] frames;
	private int pointer;
	private double elapsedTime;
	private double currentTime;
	private double lastTime;
	private double fps;

	public Animation(int amount, int fps, String filename) {
		this.pointer = 0;
		this.elapsedTime = 0;
		this.currentTime = 0;
		this.lastTime = Timer.getTime();
		this.fps = 1.0/(double) fps;

		this.frames = new Texture[amount];

		for (int i=0; i<amount; i++) {
			this.frames[i] = new Texture("anim/" + filename + "_" + i + ".png");
		}
	}

	public void bind() {
		bind(0);
	}

	public void bind(int sampler) {
		this.currentTime = Timer.getTime();
		this.elapsedTime += currentTime - lastTime;

		if (this.elapsedTime >= this.fps) {
			this.elapsedTime -= this.fps;
			this.pointer++;
		}

		if (this.pointer >= this.frames.length) {
			this.pointer = 0;
		}

		this.lastTime = this.currentTime;

		this.frames[this.pointer].bind(sampler);
	}
}
