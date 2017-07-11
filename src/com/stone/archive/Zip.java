package com.stone.archive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.stone.log.Logger;
import com.stone.thread.ThreadParams;
import com.stone.thread.ThreadUtil;

public class Zip implements IArchive {
	public static final String TAG = Zip.class.getSimpleName();

	private IArchiveListener mListener;

	@Override
	public synchronized void setListener(IArchiveListener listener) {
		mListener = listener;
	}

	@Override
	public void compressSync(File file, OutputStream output) {
		// TODO
	}

	@Override
	public void compress(final File file, final OutputStream output) {
		ThreadUtil.run(new ThreadParams(false, new Runnable() {
			@Override
			public void run() {
				compressSync(file, output);
			}
		}));
	}

	@Override
	public void decompressSync(InputStream input, File dir) {
		try {
			ZipInputStream zis = new ZipInputStream(input);
			ZipEntry entry = null;
			byte[] buf = new byte[1024 * 16];

			while ((entry = zis.getNextEntry()) != null) {
				String name = entry.getName();
				File file = new File(dir, name);
				
				if (entry.isDirectory()) {
					if (file.exists() == false) {
						file.mkdirs();
					}
				} else {
					File parent = file.getParentFile();
					if (parent.exists() == false) {
						parent.mkdirs();
					}
					
					FileOutputStream output = new FileOutputStream(file);
					int len = -1;
					while ((len = zis.read(buf)) > 0) {
						output.write(buf, 0, len);
					}
					
					output.close();
				}
				
				notityProgress(name, file.getPath());
			}
			
			zis.close();
			
			notityFinish();
		} catch (Exception e) {
			Logger.print(TAG, e);

			notityException(e);
		}
	}

	@Override
	public void decompress(final InputStream input, final File dir) {
		ThreadUtil.run(new ThreadParams(false, new Runnable() {
			@Override
			public void run() {
				decompressSync(input, dir);
			}
		}));
	}

	@Override
	public void stop() {
	}

	private void notityProgress(final String from, final String to) {
		ThreadUtil.run(new ThreadParams(true, new Runnable() {
			@Override
			public void run() {
				synchronized (Zip.this) {
					if (mListener != null) {
						mListener.onProgress(Zip.this, from, to);
					}
				}
			}
		}));
	}

	private void notityException(final Exception ex) {
		ThreadUtil.run(new ThreadParams(true, new Runnable() {
			@Override
			public void run() {
				synchronized (Zip.this) {
					if (mListener != null) {
						mListener.onException(Zip.this, ex);
					}
				}
			}
		}));
	}

	private void notityFinish() {
		ThreadUtil.run(new ThreadParams(true, new Runnable() {
			@Override
			public void run() {
				synchronized (Zip.this) {
					if (mListener != null) {
						mListener.onFinish(Zip.this);
					}
				}
			}
		}));
	}
}
