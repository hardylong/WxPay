package com.tencent.service;

import com.tencent.common.Configure;
import com.tencent.common.CustomHttpRequest;
import com.tencent.protocol.get_order_protocol.GetOrderReqData;

/**
 * Created by ZhaoTao on 2016/5/18.
 */
public class GetOrderService extends BaseService{
    public GetOrderService() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        super(Configure.GET_ORDER);
    }

    /**
     * 请求对账单下载服务
     * @param orderReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
    public String request(GetOrderReqData orderReqData) throws Exception {

        CustomHttpRequest request = new CustomHttpRequest();
        this.setServiceRequest(request);    //自定义的请求方式

        String responseString = sendPost(orderReqData);

        return responseString;
    }
}
