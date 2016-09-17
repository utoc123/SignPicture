package com.kamesuta.mc.signpic.entry;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.CountingOutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import com.kamesuta.mc.signpic.util.Downloader;

import net.minecraft.client.resources.I18n;

public class EntryDownloader {
	protected final EntryLocation location;
	protected final SignEntry entry;

	protected long maxsize;
	protected CountingOutputStream countoutput;

	public EntryDownloader(final SignEntry entry, final EntryLocation location) {
		this.location = location;
		this.entry = entry;
	}

	public void load() {
		InputStream input = null;
		this.entry.state = EntryState.LOADING;
		try {
			final File local = this.location.localLocation(this.entry.id);

			final HttpUriRequest req = new HttpGet(this.location.remoteLocation(this.entry.id));
			final HttpResponse response = Downloader.downloader.client.execute(req);
			final HttpEntity entity = response.getEntity();

			this.maxsize = entity.getContentLength();
			input = entity.getContent();
			this.countoutput = new CountingOutputStream(new BufferedOutputStream(new FileOutputStream(local)));
			IOUtils.copy(input, this.countoutput);

			this.entry.state = EntryState.LOADED;
		} catch (final URISyntaxException e) {
			this.entry.state = EntryState.ERROR;
			this.entry.advmsg = I18n.format("signpic.advmsg.invaildurl");
		} catch (final Exception e) {
			this.entry.state = EntryState.ERROR;
			this.entry.advmsg = I18n.format("signpic.advmsg.dlerror", e);
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(this.countoutput);
		}
	}

	public float getProgress() {
		if (this.maxsize > 0)
			return Math.max(0, Math.min(1, (getRate() / (float) this.maxsize)));
		return 0;
	}

	protected long getRate() {
		if (this.countoutput != null)
			return this.countoutput.getByteCount();
		return 0;
	}
}
