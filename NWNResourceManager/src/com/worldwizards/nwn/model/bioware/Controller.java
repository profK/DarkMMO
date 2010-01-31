package com.worldwizards.nwn.model.bioware;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Controller {
    public enum Type {
        Position, Orientation, Scale, Color, Radius, ShadowRadius,
        VerticalDisplacement,Multiplier, AlphaEnd, AlphaStart, BirthRate,
        Bounce_Co, ColorEnd, ColorStart,CombineTime,Drag,FPS,FrameEnd,
        FrameStart, Grav,LifeExp,Mass,P2P_Bezier2, P2P_Bezier3, PartcleRot,
        RandVel, SizeStart, SizeEnd, SizeStart_Y, SizeEnd_Y, Spread, Threshold,
        Velocity, XSize,YSize, BlurLength,LightningDelay, LightningRadius,
        LightningScale, Detonate, AlphaMid, ColorMid, PercentStart,PercentMid,
        PercentEnd, SizeMid, SizeMid_Y, SelfIllumColor, Alpha, Wirecolor;
    }
    Type controllerType;
    float[] timekeys;
    float[][] data;
    /**
     * @param controllerType
     * @param timekeys
     * @param data
     * @param trace
     */
    public Controller(Type controllerType, float[] timekeys, float[][] data, boolean trace) {
        this.controllerType = controllerType;
        this.timekeys = timekeys;
        this.data = data;
        if (trace){
            System.out.println("add controller of type = "+this.controllerType);
            for(int i=0;i<timekeys.length;i++){
                System.out.print(timekeys[i]);
                for(int j=0;j<data[i].length;j++){
                    System.out.print(" "+data[i][j]);
                }
                System.out.println();
            }
        }
    }
    /**
     * @param w
     * @param prfx
     *
     */
    public void dump(Writer w, String prfx) {
        try {
            w.write(prfx + controllerType+"\n");
            if (data.length==1){
                w.write(prfx+"   ");
                for(float f : data[0]){
                    w.write(f+" ");
                }
                w.write("\n");
            } else if (data.length>1) {
                for(int i=0;i<data.length;i++){
                    w.write(prfx+"   ");
                    w.write(timekeys[i]+": ");
                    for(float f : data[i]){
                        w.write(f+" ");
                    }
                    w.write("\n");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        }
        
    }
    
    public Type getControllerType(){
        return controllerType;
    }
    
    public float[] getTimekeys(){
        return timekeys;
    }
    
    public float[][] getData(){
        return data;
    }
}
