package com.stone.network.longconn;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.stone.log.Logger;
import com.stone.thread.ThreadParams;
import com.stone.thread.ThreadUtil;

public class LongClient {
	public static final String TAG = LongClient.class.getSimpleName();
	
	protected Socket mSocket;
	protected String mHost;
	protected int mPort;
	
	protected int mTimeout = 10;
	
	protected boolean mIsReceiving = false;
	protected final Runnable mReceiveRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				InputStream is = mSocket.getInputStream();
				
				while (true) {
					byte[] data = new byte[1024];
					int length = is.read(data);
					
					if (length > 0) {
						notifyReceived(data, 0, length);
					} else {
						Thread.sleep(100);
					}
				}
			} catch (Exception e) {
				Logger.print(TAG, e);
			}
			
			synchronized (LongClient.this) {
				mIsReceiving = false;
			}
			
			notifyDisconnected();
		}
	};
	
	protected IClientListener mListener;
	
	public LongClient() {
	}
	
	public synchronized Status getStatus() {
		if (mSocket == null) {
			return Status.None;
		}
		
		if (mSocket.isClosed()) {
			return Status.Closed;
		}
		
		if (mSocket.isConnected()) {
			return Status.Connected;
		}
		
		if (mSocket.isBound()) {
			return Status.Disconnected;
		}
		
		return Status.None;
	}
	
	public synchronized void setSocket(Socket socket) {
		mSocket = socket;
	}
	
	public synchronized void setHost(String host) {
		mHost = host;
	}
	
	public synchronized void setPort(int port) {
		mPort = port;
	}
	
	public synchronized void setTimeout(int timeout) {
		mTimeout = timeout;
	}
	
	public synchronized boolean connectSync() {
		if (mSocket == null) {
			try {
				mSocket = new Socket();
				mSocket.setSoTimeout(mTimeout);
				mSocket.connect(new InetSocketAddress(mHost, mPort));
				
				notifyConnected();
			} catch (Exception e) {
				Logger.print(TAG, e);
				
				notifyConnectFailed();
				return false;
			}
		}
		
		if (mIsReceiving) {
			return false;
		}
		mIsReceiving = true;
		
		ThreadUtil.run(new ThreadParams(false, mReceiveRunnable));
		return true;
	}
	
	public synchronized void connect() {
		ThreadUtil.run(new ThreadParams(false, new Runnable() {
			@Override
			public void run() {
				connectSync();
			}
		}));
	}
	
	public boolean sendSync(byte[] data, int offset, int length) {
		try {
			OutputStream os = mSocket.getOutputStream();
			os.write(data, offset, length);
			
			notifySended(true);
			return true;
		} catch (Exception e) {
			Logger.print(TAG, e);
		}
		
		notifySended(false);
		return false;
	}
	
	public void send(final byte[] data, final int offset, final int length) {
		ThreadUtil.run(new ThreadParams(false, new Runnable() {
			@Override
			public void run() {
				sendSync(data, offset, length);
			}
		}));
	}
	
	public synchronized boolean close() {
		try {
			if (mSocket != null && mSocket.isClosed() == false) {
				mSocket.close();
				
				notifyClosed();
				return true;
			}
		} catch (Exception e) {
			Logger.print(TAG, e);
		}
		
		return false;
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	public synchronized void setListener(IClientListener listener) {
		mListener = listener;
	}
	
	protected void notifyConnected() {
		ThreadUtil.run(new ThreadParams(false, new Runnable() {
			@Override
			public void run() {
				synchronized (LongClient.this) {
					if (mListener != null) {
						mListener.onConnected(LongClient.this);
					}
				}
			}
		}));
	}
	
	protected void notifyConnectFailed() {
		ThreadUtil.run(new ThreadParams(false, new Runnable() {
			@Override
			public void run() {
				synchronized (LongClient.this) {
					if (mListener != null) {
						mListener.onConnectFailed(LongClient.this);
					}
				}
			}
		}));
	}
	
	protected void notifyDisconnected() {
		ThreadUtil.run(new ThreadParams(false, new Runnable() {
			@Override
			public void run() {
				synchronized (LongClient.this) {
					if (mListener != null) {
						mListener.onDisconnected(LongClient.this);
					}
				}
			}
		}));
	}
	
	protected void notifyClosed() {
		ThreadUtil.run(new ThreadParams(false, new Runnable() {
			@Override
			public void run() {
				synchronized (LongClient.this) {
					if (mListener != null) {
						mListener.onClosed(LongClient.this);
					}
				}
			}
		}));
	}
	
	protected void notifySended(final boolean success) {
		ThreadUtil.run(new ThreadParams(false, new Runnable() {
			@Override
			public void run() {
				synchronized (LongClient.this) {
					if (mListener != null) {
						mListener.onSended(LongClient.this, success);
					}
				}
			}
		}));
	}
	
	protected void notifyReceived(final byte[] data, final int offset, final int length) {
		ThreadUtil.run(new ThreadParams(false, new Runnable() {
			@Override
			public void run() {
				synchronized (LongClient.this) {
					if (mListener != null) {
						mListener.onReceived(LongClient.this, data, offset, length);
					}
				}
			}
		}));
	}
}