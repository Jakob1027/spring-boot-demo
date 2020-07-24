package com.jakob.springbootdemo.rtdbus;

import com.cgs.rtd.exception.CodecException;
import com.cgs.rtd.rtdbus.RtdBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.concurrent.GuardedBy;
import java.util.HashMap;
import java.util.Map;

//@RestController
@RequestMapping("/rtdbus")
public class RtdBusController {

    @Autowired
    @GuardedBy("this")
    private RtdBus rtdBus;

    @PostMapping("/send/{tag}")
    public void send(@PathVariable("tag") String tag, @RequestBody Map<String, String> message) {
        try {
            rtdBus.write(tag, message);
        } catch (CodecException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/sendMsg/{tag}")
    public void send(@PathVariable("tag") String tag, @RequestParam("size") Integer size) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            for (int i = 0; i < size; i++) {
                map.put("timestamp", System.currentTimeMillis());
                map.put("id", i);
                rtdBus.write(tag, map);
            }
        } catch (CodecException e) {
            e.printStackTrace();
        }
    }
}
