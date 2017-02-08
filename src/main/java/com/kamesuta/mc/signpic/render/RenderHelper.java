package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.bnnwidget.render.WRenderer;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RenderHelper {
	public static void drawLoadingCircle(final int msPerRoundInner, final int msPerRoundOuter) {
		final long time = System.currentTimeMillis();
		final float time1 = time%Math.abs(msPerRoundOuter)/(float) msPerRoundOuter;
		WRenderer.w.func_181668_a(GL_LINE_LOOP, DefaultVertexFormats.field_181705_e);
		addCircleVertex(time1, time1+0.2f, 1.07f);
		addCircleVertex(time1+0.2f, time1, 1.09f);
		WRenderer.t.draw();
		final float time2 = time%Math.abs(msPerRoundInner)/(float) msPerRoundInner;
		WRenderer.w.func_181668_a(GL_LINE_LOOP, DefaultVertexFormats.field_181705_e);
		addCircleVertex(time2, time2+0.1f, 1.03f);
		addCircleVertex(time2+0.1f, time2, 1.05f);
		WRenderer.t.draw();
	}

	public static void drawDesignCircle() {
		WRenderer.w.func_181668_a(GL_LINE_LOOP, DefaultVertexFormats.field_181705_e);
		addCircleVertex(0f, 1f, 1f);
		WRenderer.t.draw();
	}

	public static void drawProgressCircle(final float progress) {
		WRenderer.w.func_181668_a(GL_POLYGON, DefaultVertexFormats.field_181705_e);
		WRenderer.w.func_181662_b(0f, 0f, 0f).func_181675_d();
		addCircleVertex(progress, 0f, 1f);
		WRenderer.t.draw();
	}

	public static void drawProgressCircle(final int mode, final float r) {
		WRenderer.w.func_181668_a(mode, DefaultVertexFormats.field_181705_e);
		addCircleVertex(0f, 1f, r);
		WRenderer.t.draw();
	}

	public static void addCircleVertex(final float start, final float end, final float r, final float acc) {
		final double sangle = Math.PI*(2d*start-.5);
		final double sx = Math.cos(sangle);
		final double sy = Math.sin(sangle);
		final double eangle = Math.PI*(2d*end-.5);
		final double ex = Math.cos(eangle);
		final double ey = Math.sin(eangle);

		WRenderer.w.func_181662_b(sx*r, sy*r, 0).func_181675_d();
		for (int i = (int) (end<start ? Math.floor(start*acc) : Math.ceil(start*acc)); end<start ? i>end*acc : i<end*acc; i += end<start ? -1 : 1) {
			final double angle = Math.PI*(2d*i/acc-.5);
			final double ix = Math.cos(angle);
			final double iy = Math.sin(angle);
			WRenderer.w.func_181662_b(ix*r, iy*r, 0).func_181675_d();
		}
		WRenderer.w.func_181662_b(ex*r, ey*r, 0).func_181675_d();
	}

	public static void addCircleVertex(final float start, final float end, final float r) {
		addCircleVertex(start, end, r, 32f);
	}

	public static void drawRect(final int mode) {
		drawRect(mode, 0, 0, 1, 1);
	}

	public static void drawRect(final int mode, final float x1, final float y1, final float x2, final float y2) {
		WRenderer.w.func_181668_a(mode, DefaultVertexFormats.field_181707_g);
		addRectVertex(x1, y1, x2, y2);
		WRenderer.t.draw();
	}

	public static void addRectVertex(final float x1, final float y1, final float x2, final float y2) {
		WRenderer.w.func_181662_b(x1, y2, 0).func_181673_a(0, 0).func_181675_d();
		WRenderer.w.func_181662_b(x2, y2, 0).func_181673_a(0, 1).func_181675_d();
		WRenderer.w.func_181662_b(x2, y1, 0).func_181673_a(1, 1).func_181675_d();
		WRenderer.w.func_181662_b(x1, y1, 0).func_181673_a(1, 0).func_181675_d();
	}

	public static void drawRectTexture(final int mode) {
		drawRectTexture(mode, 0, 0, 1, 1, 0, 0, 1, 1);
	}

	public static void drawRectTexture(final int mode, final float x1, final float y1, final float x2, final float y2, final float u1, final float v1, final float u2, final float v2) {
		WRenderer.w.func_181668_a(mode, DefaultVertexFormats.field_181707_g);
		addRectVertexTexture(x1, y1, x2, y2, u1, v1, u2, v2);
		WRenderer.t.draw();
	}

	public static void addRectVertexTexture(final float x1, final float y1, final float x2, final float y2, final float u1, final float v1, final float u2, final float v2) {
		WRenderer.w.func_181662_b(x1, y2, 0).func_181673_a(u1, v2).func_181675_d();
		WRenderer.w.func_181662_b(x2, y2, 0).func_181673_a(u2, v2).func_181675_d();
		WRenderer.w.func_181662_b(x2, y1, 0).func_181673_a(u2, v1).func_181675_d();
		WRenderer.w.func_181662_b(x1, y1, 0).func_181673_a(u1, v1).func_181675_d();
	}
}