package net.worldwizards.darkmmo.tools

/**
 * Created by IntelliJ IDEA.
 * User: jeff
 * Date: Dec 24, 2009
 * Time: 4:28:26 PM
 * To change this template use File | Settings | File Templates.
 */

class IteratorWrapper[A](iter:java.util.Iterator[A])
{
    def foreach(f: A => Unit): Unit = {
        while(iter.hasNext){
          f(iter.next)
        }
    }
}