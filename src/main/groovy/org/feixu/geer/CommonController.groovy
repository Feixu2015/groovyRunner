package org.feixu.geer

import com.alibaba.fastjson.JSONObject
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/common")
class CommonController {
    @PostMapping("/lock")
    public String objLock(@RequestBody JSONObject body) {
        body.get('id')
    }
}
