package com.worldwizards.util;

import java.io.Reader;
import java.nio.CharBuffer;

public class CharBufferReader extends Reader{
  CharBuffer buff;
  public CharBufferReader(CharBuffer buffer) {
    buff = buffer;
  }

  /**
   * close
   */
  public void close() {
  }

  /**
   * read
   *
   * @param cbuf char[]
   * @param off int
   * @param len int
   * @return int
   */
  public int read(char[] cbuf, int off, int len) {
    if (buff.remaining() == 0 ) {
      return -1;
    }
    if (buff.remaining()<len) {
      len = buff.remaining();
    }
    buff.get(cbuf,off,len);
    return len;
  }

  /**
    * Mark the present position in the stream.  Subsequent calls to reset()
    * will attempt to reposition the stream to this point.  Not all
    * character-input streams support the mark() operation.
    *
    * @param  readAheadLimit  Limit on the number of characters that may be
    *                         read while still preserving the mark.  After
    *                         reading this many characters, attempting to
    *                         reset the stream may fail.
    *
    * @exception  IOException  If the stream does not support mark(),
    *                          or if some other I/O error occurs
    */
   public void mark(int readAheadLimit) {
       buff.mark();
   }


  /**
     * Reset the stream.  If the stream has been marked, then attempt to
     * reposition it at the mark.  If the stream has not been marked, then
     * attempt to reset it in some way appropriate to the particular stream,
     * for example by repositioning it to its starting point.  Not all
     * character-input streams support the reset() operation, and some support
     * reset() without supporting mark().
     *
     * @exception  IOException  If the stream has not been marked,
     *                          or if the mark has been invalidated,
     *                          or if the stream does not support reset(),
     *                          or if some other I/O error occurs
     */
    public void reset()  {
        buff.reset();
    }

    /**
    * Tell whether this stream is ready to be read.
    *
    * @return True if the next read() is guaranteed not to block for input,
    * false otherwise.  Note that returning false does not guarantee that the
    * next read will block.
    *
    * @exception  IOException  If an I/O error occurs
    */
   public boolean ready() {
       return buff.remaining()>0;
   }

   /**
    * Tell whether this stream supports the mark() operation. The default
    * implementation always returns false. Subclasses should override this
    * method.
    *
    * @return true if and only if this stream supports the mark operation.
    */
   public boolean markSupported() {
       return true;
   }



}
