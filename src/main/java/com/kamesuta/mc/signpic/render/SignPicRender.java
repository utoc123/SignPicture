package com.kamesuta.mc.signpic.render;

import static org.lwjgl.opengl.GL11.*;

import com.kamesuta.mc.bnnwidget.WGui;
import com.kamesuta.mc.signpic.Client;
import com.kamesuta.mc.signpic.Config;
import com.kamesuta.mc.signpic.entry.Entry;
import com.kamesuta.mc.signpic.entry.EntryId;
import com.kamesuta.mc.signpic.entry.content.Content;
import com.kamesuta.mc.signpic.handler.CoreEvent;
import com.kamesuta.mc.signpic.image.meta.ImageSize;
import com.kamesuta.mc.signpic.mode.CurrentMode;
import com.kamesuta.mc.signpic.util.Sign;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class SignPicRender extends WGui {
	public static final ResourceLocation resSign = new ResourceLocation("textures/items/sign.png");

	public SignPicRender() {
	}

	@CoreEvent
	public void onRender(final RenderWorldLastEvent event) {
		float opacity = Config.instance.renderPreviewFixedOpacity;
		if (CurrentMode.instance.isMode(CurrentMode.Mode.SETPREVIEW) || CurrentMode.instance.isMode(CurrentMode.Mode.PLACE)) {
			Sign.preview.capturePlace();
			opacity = Config.instance.renderPreviewFloatedOpacity;
		}
		if (CurrentMode.instance.isState(CurrentMode.State.PREVIEW)) {
			if (Sign.preview.isRenderable() && Sign.preview.isVisible()) {
				final TileEntitySign tile = Sign.preview.getRenderTileEntity();
				final BlockPos pos = tile.getPos();
				Client.renderer.renderSignPictureBase(tile, pos.getX() - TileEntityRendererDispatcher.staticPlayerX, pos.getY() - TileEntityRendererDispatcher.staticPlayerY, pos.getZ() - TileEntityRendererDispatcher.staticPlayerZ, event.getPartialTicks(), -1, opacity);
			}
		}
	}

	@CoreEvent
	public void onDraw(final RenderGameOverlayEvent.Post event) {
		if(event.getType() == ElementType.EXPERIENCE)
			if (CurrentMode.instance.isMode()) {
				if ((int)(System.currentTimeMillis()/500)%2==0) {
					final FontRenderer fontrenderer = font();

					RenderHelper.startTexture();
					GlStateManager.pushMatrix();
					GlStateManager.translate(5f, 5f, 0f);
					GlStateManager.scale(2f, 2f, 1f);

					GlStateManager.pushMatrix();
					GlStateManager.scale(fontrenderer.FONT_HEIGHT, fontrenderer.FONT_HEIGHT, 1f);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

					texture().bindTexture(resSign);
					RenderHelper.startTexture();
					RenderHelper.drawRectTexture(GL_QUADS);

					GlStateManager.popMatrix();

					GlStateManager.translate(fontrenderer.FONT_HEIGHT, 0f, 0f);
					final String str = I18n.format(CurrentMode.instance.getMode().message);
					fontrenderer.drawStringWithShadow(str, 0, 0, 0xffffff);

					GlStateManager.popMatrix();
				}
			}
	}

	@CoreEvent
	public void onText(final RenderGameOverlayEvent.Text event) {
		if (Client.mc.gameSettings.showDebugInfo) {
			final TileEntitySign tilesign = Client.getTileSignLooking();
			if (tilesign != null) {
				final Entry entry = EntryId.fromTile(tilesign).entry();
				if (entry.isValid()) {
					final String uri = entry.contentId.getURI();
					final ImageSize signsize = entry.meta.size;
					final Content content = entry.content();
					final ImageSize imagesize = content.image.getSize();
					final ImageSize viewsize = new ImageSize().setAspectSize(entry.meta.size, imagesize);
					final String advmsg = content.state.getMessage();

					event.getLeft().add("");
					event.getLeft().add(I18n.format("signpic.over.sign", entry.id.id()));
					event.getLeft().add(I18n.format("signpic.over.id", uri));
					event.getLeft().add(I18n.format("signpic.over.size", signsize, signsize.width, signsize.height, imagesize.width, imagesize.height, viewsize.width, viewsize.height));
					event.getLeft().add(I18n.format("signpic.over.status", content.state.getStateMessage()));
					if (advmsg != null)
						event.getLeft().add(I18n.format("signpic.over.advmsg", advmsg));
					if (tilesign.signText != null)
						event.getLeft().add(I18n.format("signpic.over.raw", tilesign.signText[0].getUnformattedText(), tilesign.signText[1].getUnformattedText(), tilesign.signText[2].getUnformattedText(), tilesign.signText[3].getUnformattedText()));
					event.getLeft().add(I18n.format("signpic.over.local", content.image.getLocal()));
				}
			}
		}
	}
}