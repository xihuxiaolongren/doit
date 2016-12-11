package me.xihuxiaolong.library.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUtils {
	
	/**  
     * Description：<code> 处理过去时间显示效果 </code> By 古木天琪 at 2012-10-30 下午5:16:37  
     * @param createAt  
     *            Data 类型  
     * @return { String }
     * @throws  
     */  
    public static String getInterval(long createAt) {
        //定义最终返回的结果字符串。   
        String interval = null;
  
        Calendar dateCalendar = Calendar.getInstance();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateCalendar.setTimeInMillis(createAt);
        dateCalendar.set(Calendar.HOUR_OF_DAY, 0);
        dateCalendar.set(Calendar.MINUTE, 0);
        //格式化输出  
	    System.out.println(df.format(dateCalendar.getTime()));
	    
        Calendar todayCalendar = Calendar.getInstance();
        long now = System.currentTimeMillis();
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        todayCalendar.set(Calendar.MINUTE, 0);
        System.out.println(df.format(todayCalendar.getTime()));
        long intervalDays = (todayCalendar.getTimeInMillis()/ 86400000 - dateCalendar.getTimeInMillis()/ 86400000) ;
        if(intervalDays <= 0){	//今天
        	long second = (now - createAt) / 1000;
            if (second <= 0) {   
                second = 0;   
            }   
            
            if (second == 0) {   
                interval = "刚刚";   
            } else if (second < 30) {   
                interval = second + "秒以前";   
            } else if (second >= 30 && second < 60) {   
                interval = "半分钟前";   
            } else if (second >= 60 && second < 60 * 60) {   
                long minute = second / 60;   
                interval = minute + "分钟前";   
            } else if (second >= 60 * 60 && second < 60 * 60 * 24) {   
                long hour = (second / 60) / 60;   
                if (hour <= 12) {   
                    interval = hour + "小时前";   
                } else {   
                    interval = "今天" + getFormatTime(createAt, "HH:mm");   
                }   
            }
        }else if(intervalDays == 1){	// 判断是不是昨天
        	interval = "昨天" + getFormatTime(createAt, "HH:mm");
        }else{
        	interval = getFormatTime(createAt, "MM月dd日 HH:mm");
        }
        // 最后返回处理后的结果。   
        return interval;   
    }
    
    /** 
     * 得到日期   yyyy-MM-dd 
     * @param timeStamp  时间戳 
     * @return 
     */  
    public static String getFormatTime(long timeStamp, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String date = sdf.format(timeStamp);
        return date;  
    }  

}
