package com.android.fisewatchlauncher.event;

import com.android.fisewatchlauncher.entity.dao.StepHistory;

/**
 * @author mare
 * @Description:
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/8/31
 * @time 11:32
 */
public class StepUpdateEvent {
   public StepHistory history;

   public StepUpdateEvent(StepHistory history) {
      this.history = history;
   }

   @Override
   public String toString() {
      return "StepUpdateEvent{" +
              "history=" + history.toString() +
              '}';
   }
}
