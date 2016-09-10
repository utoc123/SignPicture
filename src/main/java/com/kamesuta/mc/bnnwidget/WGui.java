package com.kamesuta.mc.bnnwidget;

import static org.lwjgl.opengl.GL11.*;

import org.apache.commons.lang3.StringUtils;

import com.kamesuta.mc.bnnwidget.position.Area;
import com.kamesuta.mc.bnnwidget.position.Coord;
import com.kamesuta.mc.bnnwidget.position.RArea;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;

public class WGui extends Gui {
	public static final Minecraft mc;
	public static final Tessellator t = Tessellator.getInstance();
	public static final VertexBuffer w = t.getBuffer();
	public static final StencilClip clip = StencilClip.instance;
	static {
		mc = FMLClientHandler.instance().getClient();
	}

	public static TextureManager texture() {
		return mc.renderEngine;
	}

	public static FontRenderer font() {
		return mc.fontRendererObj;
	}

	public static void drawHorizontalLine(final float x1, final float x2, final float y, final int color)
	{
		drawRect(Math.min(x1, x2), y, Math.max(x1, x2) + 1, y + 1, color);
	}

	public static void drawVerticalLine(final float x, final float y1, final float y2, final int color)
	{
		drawRect(x, Math.min(y1, y2) + 1, x + 1, Math.max(y1, y2), color);
	}

	/**
	 * Draws a solid color rectangle with the specified coordinates and color. Args: x1, y1, x2, y2, color
	 */
	public static void drawRect(final float x1, final float y1, final float x2, final float y2, final int color)
	{
		final float a = (color >> 24 & 255) / 255.0F;
		final float r = (color >> 16 & 255) / 255.0F;
		final float g = (color >> 8 & 255) / 255.0F;
		final float b = (color & 255) / 255.0F;
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.color(r, g, b, a);
		w.begin(GL_QUADS, DefaultVertexFormats.POSITION);
		w.pos(Math.min(x1, x2), Math.max(y1, y2), 0.0D).endVertex();
		w.pos(Math.max(x1, x2), Math.max(y1, y2), 0.0D).endVertex();
		w.pos(Math.max(x1, x2), Math.min(y1, y2), 0.0D).endVertex();
		w.pos(Math.min(x1, x2), Math.min(y1, y2), 0.0D).endVertex();
		t.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	/**
	 * Draws a rectangle with a vertical gradient between the specified colors.
	 */
	public static void drawGradientRect(final float left, final float top, final float right, final float bottom, final int startColor, final int endColor)
	{
		final float f = (startColor >> 24 & 255) / 255.0F;
		final float f1 = (startColor >> 16 & 255) / 255.0F;
		final float f2 = (startColor >> 8 & 255) / 255.0F;
		final float f3 = (startColor & 255) / 255.0F;
		final float f4 = (endColor >> 24 & 255) / 255.0F;
		final float f5 = (endColor >> 16 & 255) / 255.0F;
		final float f6 = (endColor >> 8 & 255) / 255.0F;
		final float f7 = (endColor & 255) / 255.0F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.shadeModel(7425);
		w.begin(7, DefaultVertexFormats.POSITION_COLOR);
		w.pos(right, top, 0).color(f1, f2, f3, f).endVertex();
		w.pos(left, top, 0).color(f1, f2, f3, f).endVertex();
		w.pos(left, bottom, 0).color(f5, f6, f7, f4).endVertex();
		w.pos(right, bottom, 0).color(f5, f6, f7, f4).endVertex();
		t.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}

	/**
	 * Renders the specified text to the screen, center-aligned.
	 */
	public static void drawCenteredString(final String text, final float x, final float y, final int color)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 0f);
		font().drawStringWithShadow(text, - font().getStringWidth(text) / 2, 0, color);
		GlStateManager.popMatrix();
	}

	/**
	 * Renders the specified text to the screen.
	 */
	public static void drawString(final String text, final float x, final float y, final int color)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 0f);
		font().drawStringWithShadow(text, 0, 0, color);
		GlStateManager.popMatrix();
	}

	/**
	 * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
	 */
	public static void drawTexturedModalRect(final float x, final float y, final float u, final float v, final float width, final float height)
	{
		final float f = 0.00390625F;
		final float f1 = 0.00390625F;
		w.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		w.pos(x + 0, y + height, 0).tex((u + 0) * f, (v + height) * f1).endVertex();
		w.pos(x + width, y + height, 0).tex((u + width) * f, (v + height) * f1).endVertex();
		w.pos(x + width, y + 0, 0).tex((u + width) * f, (v + 0) * f1).endVertex();
		w.pos(x + 0, y + 0, 0).tex((u + 0) * f, (v + 0) * f1).endVertex();
		t.draw();
	}

	public static void drawTexturedModelRectFromIcon(final float x, final float y, final TextureAtlasSprite image, final float width, final float height)
	{
		w.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		w.pos(x + 0, y + height, 0).tex(image.getMinU(), image.getMaxV()).endVertex();
		w.pos(x + width, y + height, 0).tex(image.getMaxU(), image.getMaxV()).endVertex();
		w.pos(x + width, y + 0, 0).tex(image.getMaxU(), image.getMinV()).endVertex();
		w.pos(x + 0, y + 0, 0).tex(image.getMinU(), image.getMinV()).endVertex();
		t.draw();
	}

	public static void drawTexturedModelRect(final float x, final float y, final float u, final float v, final float width, final float height, final float divu, final float divv)
	{
		final float mulu = 1.0F / divu;
		final float mulv = 1.0F / divv;
		w.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		w.pos(x, y + height, 0.0D).tex(u * mulu, (v + height) * mulv).endVertex();
		w.pos(x + width, y + height, 0.0D).tex((u + width) * mulu, (v + height) * mulv).endVertex();
		w.pos(x + width, y, 0.0D).tex((u + width) * mulu, v * mulv).endVertex();
		w.pos(x, y, 0.0D).tex(u * mulu, v * mulv).endVertex();
		t.draw();
	}

	public static void drawModalRectWithCustomSizedTexture(final float x, final float y, final float u, final float v, final float uwidth, final float vheight, final float width, final float height, final float divu, final float divv)
	{
		final float mulu = 1.0F / divu;
		final float mulv = 1.0F / divv;
		w.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		w.pos(x, y + height, 0.0D).tex(u * mulu, (v + vheight) * mulv).endVertex();
		w.pos(x + width, y + height, 0.0D).tex((u + uwidth) * mulu, (v + vheight) * mulv).endVertex();
		w.pos(x + width, y, 0.0D).tex((u + uwidth) * mulu, v * mulv).endVertex();
		w.pos(x, y, 0.0D).tex(u * mulu, v * mulv).endVertex();
		t.draw();
	}

	public static void draw(final Area p, final int mode) {
		w.begin(mode, DefaultVertexFormats.POSITION);
		w.pos(p.x1(), p.y1(), 0);
		w.pos(p.x1(), p.y2(), 0);
		w.pos(p.x2(), p.y2(), 0);
		w.pos(p.x2(), p.y1(), 0);
		t.draw();
	}

	public static RArea horizontalLine = new RArea(Coord.left(0), Coord.top(0), Coord.right(0), Coord.top(1));
	public static void drawHorizontalLine(final Area a)
	{
		drawRect(a.child(horizontalLine));
	}

	public static RArea verticalLine = new RArea(Coord.left(0), Coord.top(0), Coord.left(1), Coord.bottom(0));
	public static void drawVerticalLine(final Area a)
	{
		drawRect(a.child(verticalLine));
	}

	public static void drawRect(final Area a)
	{
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		w.begin(7, DefaultVertexFormats.POSITION);
		w.pos(a.x1(), a.y2(), .0);
		w.pos(a.x2(), a.y2(), .0);
		w.pos(a.x2(), a.y1(), .0);
		w.pos(a.x1(), a.y1(), .0);
		t.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public static void drawRect(final Area a, final int color)
	{
		drawRect(a.x1(), a.y1(), a.x2(), a.y2(), color);
	}

	/**
	 * Draws a rectangle with a vertical gradient between the specified colors.
	 */
	public static void drawGradientRect(final Area a, final int color1, final int color2)
	{
		drawGradientRect(a.x1(), a.y1(), a.x2(), a.y2(), color1, color2);
	}

	/**
	 * Renders the specified text to the screen, center-aligned.
	 */
	public static void drawCenteredString(final String text, final Area a, final int color)
	{
		drawCenteredString(text, a.x1(), a.y1(), color);
	}

	/**
	 * Renders the specified text to the screen.
	 */
	public static void drawString(final String text, final Area a, final int color)
	{
		drawString(text, a.x1(), a.y1(), color);
	}

	/**
	 * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
	 */
	public static void drawTexturedModalRect(final Area a, final float u, final float v)
	{
		drawTexturedModalRect(a.x1(), a.y1(), u, v, a.w(), a.h());
	}

	/**
	 * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
	 */
	public static void drawTexturedModalRect(final Area a, final Area texture)
	{
		final float f = 0.00390625F;
		final float f1 = 0.00390625F;
		w.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		w.pos(a.x1(), a.y2(), 0).tex(texture.x1() * f, texture.y2() * f1).endVertex();
		w.pos(a.x2(), a.y2(), 0).tex(texture.x2() * f, texture.y2() * f1).endVertex();
		w.pos(a.x2(), a.y1(), 0).tex(texture.x2() * f, texture.y1() * f1).endVertex();
		w.pos(a.x1(), a.y1(), 0).tex(texture.x1() * f, texture.y1() * f1).endVertex();
		t.draw();
	}

	public static void drawTexturedModelRectFromIcon(final Area a, final TextureAtlasSprite icon)
	{
		drawTexturedModelRectFromIcon(a.x1(), a.y1(), icon, a.w(), a.h());
	}

	public static void translate(final Area p) {
		GlStateManager.translate(p.x1(), p.y1(), 0f);
	}

	public static void drawString(final String text, final int x, final int y, final int colour, final boolean shadow) {
		if (shadow)
			font().drawStringWithShadow(text, x, y, colour);
		else
			font().drawString(text, x, y, colour);
	}

	public static void drawString(final String text, final int x, final int y, final int colour) {
		drawString(text, x, y, colour, true);
	}

	public static void drawStringC(final String text, final int x, final int y, final int w, final int h, final int colour, final boolean shadow) {
		drawString(text, x + (w - getStringWidth(text)) / 2, y + (h - 8) / 2, colour, shadow);
	}

	public static void drawStringC(final String text, final int x, final int y, final int w, final int h, final int colour) {
		drawStringC(text, x, y, w, h, colour, true);
	}

	public static void drawStringC(final String text, final int x, final int y, final int colour, final boolean shadow) {
		drawString(text, x - getStringWidth(text) / 2, y, colour, shadow);
	}

	public static void drawStringC(final String text, final int x, final int y, final int colour) {
		drawStringC(text, x, y, colour, true);
	}

	public static void drawStringR(final String text, final int x, final int y, final int colour, final boolean shadow) {
		drawString(text, x - getStringWidth(text), y, colour, shadow);
	}

	public static void drawStringR(final String text, final int x, final int y, final int colour) {
		drawStringR(text, x, y, colour, true);
	}

	public static int getStringWidth(final String s) {
		if (StringUtils.isEmpty(s))
			return 0;
		return font().getStringWidth(TextFormatting.getTextWithoutFormattingCodes(s));
	}
}
