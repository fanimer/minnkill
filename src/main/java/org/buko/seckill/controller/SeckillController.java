package org.buko.seckill.controller;

import org.apache.log4j.Logger;
import org.buko.seckill.dto.Exposer;
import org.buko.seckill.dto.SeckillExecution;
import org.buko.seckill.dto.SeckillResult;
import org.buko.seckill.entity.Seckill;
import org.buko.seckill.enums.SeckillStatEnum;
import org.buko.seckill.exception.RepeatKillException;
import org.buko.seckill.exception.SeckillCloseException;
import org.buko.seckill.service.SeckillService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/seckill")
public class SeckillController {
    private final Logger logger = Logger.getLogger(this.getClass());

    @Resource
    private SeckillService seckillService;

    @GetMapping(value = "/list")
    public String list(Model model) {
        List<Seckill> list = seckillService.getSerkillList();
        model.addAttribute("list", list);
        return "list";
    }

    @GetMapping(value = "/{seckillId}/detail")
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) return "redirect:/seckill/list";
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) return "redirect:/seckill/list";
        model.addAttribute("seckill", seckill);
        return "details";
    }

    @PostMapping(value = "/{seckillId}/exposer", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            e.printStackTrace();
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }

    @PostMapping(value = "/{seckillId}/{md5}/execution", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                    @PathVariable("md5") String md5,
                                                    @CookieValue(value = "killPhone", required = false) Long phone) {
        if(phone == null) return new SeckillResult<SeckillExecution>(false, "未注册");
        SeckillResult<SeckillExecution> result;
        try {
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (RepeatKillException e) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (SeckillCloseException e) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (Exception e) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true, execution);
        }
    }

    @GetMapping(value = "/time/now", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult<Long>(true, now.getTime());
    }

    @RequestMapping("/test")
    public String test() {
        return "list";
    }

}
