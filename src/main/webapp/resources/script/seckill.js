var seckill = {
    URL: {
        now: function () {
            return '/minnkill/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/minnkill/seckill/' + seckillId + '/exposer';
        },
        execution: function (seckillId, md5) {
            return '/minnkill/seckill/' +seckillId + '/' + md5 + '/execution';
        }
    },

    validatePhone: function (phone) {
        return phone && phone.length === 11 && !isNaN(phone);
    },

    detail: {
        // 详情页初始化
        init: function (params) {
            // 从 cookie 中获取 phone number
            var killPhone = $.cookie('killPhone');
            console.log(killPhone);
            // 如果 cookie 中无 phone number，则通过交互获取
            if (!seckill.validatePhone(killPhone)) {
                var killPhoneModel = $('#killPhoneModel');
                console.log("killPhoneModel")
                killPhoneModel.modal({
                    show:true,
                    backdrop:'static',
                    keyboard:false
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    console.log("inputPhone:" + inputPhone);
                    // 获取输入phone number
                    if(seckill.validatePhone(inputPhone)) {
                        // 写入cookie，期限7天
                        $.cookie('killPhone', inputPhone, {expires: 7, path:'/minnkill/seckill'});
                        window.location.reload();//刷新页面
                    }
                    else $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误</label>').show(300);
                });
            }

            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            // 获取时间
            $.get(seckill.URL.now(), {}, function (result) {
                if(result && result['success']) {
                    var nowTime = result['data'];
                    seckill.countDown(seckillId, nowTime, startTime, endTime);
                } else {
                    console.log('result:' + result);
                    alert('result:' + result);
                }
            })
        }
    },

    handlerSeckill: function (seckillId, node) {
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            console.log(result);
            if(result && result['success']) {
                var exposer = result['data'];
                console.log(exposer['exposed']);
                if(exposer['exposed']) {
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log("killUrl: " + killUrl);
                    $('#killBtn').one('click', function () {
                        $(this).addClass('disabled');
                        $.post(killUrl, {}, function (result) {
                            if(result && result['success']) {
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }
                        });
                    });
                    node.show();
                } else {
                    console.log('???');
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    seckill.countDown(seckillId, now, start, end);
                }
            } else console.log('result:' + result);
        });
    },

    countDown: function (seckillId, nowTime, startTime, endTime) {
        console.log(seckillId + '_' + nowTime + '_' + startTime + '_' + endTime);
        var seckillBox = $('#seckill-box');
        if (nowTime > endTime) seckillBox.html('秒杀结束!');
        else if (nowTime < startTime) {
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime, function (event) {
                var format = event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒 ');
                console.log(format);
                seckillBox.html(format);
            }).on('finish.countdown', function () {
                console.log('____finish.countdown');
                seckill.handlerSeckill(seckillId, seckillBox);
            })
        } else seckill.handlerSeckill(seckillId, seckillBox);
    }

}