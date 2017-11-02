package uk.co.ribot.androidboilerplate.data.remote.gsonconverterfactory;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import uk.co.ribot.androidboilerplate.data.model.net.exception.ApiException;
import uk.co.ribot.androidboilerplate.data.model.net.httpstatus.HttpStatus;
import uk.co.ribot.androidboilerplate.data.model.net.response.base.BaseResponse;

/**
 * Created by mike on 2017/10/11.
 */

public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;


    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);
        String errorMessage = "";
        String state = "";
        int errorCode = 0;
        if (baseResponse.getResult() != null) {
            errorMessage = baseResponse.getResult().getError();
            state = baseResponse.getResult().getState();
        } else {
            if (baseResponse.getError() != null) {
                errorMessage = baseResponse.getError().getMessage();
                errorCode = baseResponse.getError().getCode();
            }
        }
        HttpStatus httpStatus = new HttpStatus(state, errorMessage);
        if (httpStatus.isCodeInvalid()) {
            value.close();
            throw new ApiException(errorCode, errorMessage);
        }
        //判断是否有数据体返回
        if (baseResponse.getResult() != null ) {
            try {
                JSONObject responseJson = new JSONObject(response);
                JSONObject dataJson;
                if (baseResponse.getResult().getData() != null){
                    dataJson =  responseJson.optJSONObject("result").optJSONObject("data");
                }else{
                    //有数据返回,但是没有data这个key
                    dataJson =  responseJson.optJSONObject("result");
                }
                StringReader stringReader = new StringReader(dataJson.toString());
                try {
                    return adapter.read(gson.newJsonReader(stringReader));
                } finally {
                    value.close();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //没有数据体返回,不做处理，原样返回
        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}
