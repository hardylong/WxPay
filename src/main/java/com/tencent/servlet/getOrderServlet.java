package com.tencent.servlet;

import com.alibaba.fastjson.JSONObject;
import com.tencent.WXPay;
import com.tencent.common.Signature;
import com.tencent.protocol.BaseProtocol;
import com.tencent.protocol.get_order_protocol.GetOrderReqData;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Created by ZhaoTao on 2016/5/17.
 * 统一下单
 */
//getOrderQR
public class getOrderServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String acceptjson = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (ServletInputStream) request.getInputStream(), "utf-8"));

            StringBuffer sb = new StringBuffer("");
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            br.close();
            acceptjson = sb.toString();
            BaseProtocol baseData =  JSONObject.parseObject(acceptjson,BaseProtocol.class);  //接收到的数据

            //统一下单请求的数据
            GetOrderReqData getOrderReqData = new GetOrderReqData(baseData.getSubMchId(),baseData.getBody(),baseData.getAttach(),
                    baseData.getOutTradeNo(),baseData.getTotalFee(),baseData.getDeviceInfo(),baseData.getSpBillCreateIP(),
                    baseData.getTimeStart(),baseData.getTimeExpire(),baseData.getNotifyUrl(),baseData.getTradeType(),
                    baseData.getProductId());

            String fromApi;

            try {
                fromApi = WXPay.requestGetOrderService(getOrderReqData); //向微信发起请求
            } catch (Exception e) {
                fromApi = "请求失败";
            }

            System.out.println(fromApi);

            String returnMsg;   //返回信息

            if(Signature.checkIsSignValidFromResponseString(fromApi)){  //验证签名
                returnMsg = fromApi;
            }else {
                returnMsg = "签名错误"; //错误信息处理
            }

            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml;charset=UTF-8");

            PrintWriter out = response.getWriter();
            out.print(returnMsg);

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
