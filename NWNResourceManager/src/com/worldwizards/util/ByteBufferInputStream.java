package com.worldwizards.util;

import java.nio.ByteBuffer;
import java.io.IOException;
import java.io.InputStream;

public class ByteBufferInputStream extends InputStream{
  ByteBuffer buff;
  
  
  public ByteBufferInputStream(ByteBuffer buff) {
    this.buff = buff;
  }

  /**
   * read
   *
   * @return int
   */
  public int read() {
    if (buff.hasRemaining()){  
        return buff.get();
    } else {
        return -1;
    }
  }
  
    @Override
  public int read(byte[] array) {
    if (buff.hasRemaining()){  
        int amntRead = array.length;
        if (amntRead > buff.remaining()){
            amntRead = buff.remaining();
        }
        buff.get(array,0,amntRead);
        return amntRead;
    } else {
        return -1;
    }
  }
    
  
  
    @Override
	public void close() throws IOException {
		// do nothing
	}

	@Override
	public synchronized void mark(int arg0) {
		// TODO Auto-generated method stub
		buff.mark();
	}

	@Override
	public boolean markSupported() {
		return true;
	}

	@Override
	public int read(byte[] dst, int offset, int length) throws IOException {
		if (length>available()){
			length=available();
		}
		buff.get(dst, offset, length);
		return length;
	}

	@Override
	public synchronized void reset() throws IOException {
		// TODO Auto-generated method stub
		buff.position(0);
	}

	@Override
	public long skip(long arg0) throws IOException {
		// TODO Auto-generated method stub
		return super.skip(arg0);
	}

	@Override
  public int available(){
      return buff.remaining();
  }
}
