package com.houoy.www.gongxing.mock;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.houoy.www.gongxing.vo.ResultVO;
import com.houoy.www.gongxing.model.messageInfo.Data;

/**
 * Created by andyzhao on 2017/12/23.
 */

public class MockData {
    public static Data getInfoData() {
        String json = "{\n" +
                "    \"code\": \"success\",\n" +
                "    \"message\": \"操作成功\",\n" +
                "    \"data\": {\n" +
                "        \"ClientInfo\": {\n" +
                "            \"UserID\": \"12345678\",\n" +
                "            \"MsgType\": \"1\",\n" +
                "            \"RelationID\": \"123\",\n" +
                "            \"IDCode\": \"123\"\n" +
                "        },\n" +
                "        \"DataPart\": {\n" +
                "            \"Place\": [\n" +
                "                {\n" +
                "                    \"PlaceName\": \"图书馆机房\",\n" +
                "                    \"PlaceId\": \"15\",\n" +
                "                    \"DeviceInfo\": [\n" +
                "                        {\n" +
                "                            \"DeviceName\": \"温湿度2\",\n" +
                "                            \"BackColor\": \"\",\n" +
                "                            \"FontColor\": \"\",\n" +
                "                            \"ParaInfo\": [\n" +
                "                                {\n" +
                "                                    \"ParaName\": {\n" +
                "                                        \"Name\": \"温度\",\n" +
                "                                        \"BackColor\": \"\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    },\n" +
                "                                    \"ParaValue\": {\n" +
                "                                        \"Name\": \"22.5℃\",\n" +
                "                                        \"BackColor\": \"\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    },\n" +
                "                                    \"ParaState\": {\n" +
                "                                        \"Name\": \"正常\",\n" +
                "                                        \"BackColor\": \"\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    }\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"ParaName\": {\n" +
                "                                        \"Name\": \"湿度\",\n" +
                "                                        \"BackColor\": \"\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    },\n" +
                "                                    \"ParaValue\": {\n" +
                "                                        \"Name\": \"45%\",\n" +
                "                                        \"BackColor\": \"\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    },\n" +
                "                                    \"ParaState\": {\n" +
                "                                        \"Name\": \"正常\",\n" +
                "                                        \"BackColor\": \"\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    }\n" +
                "                                }\n" +
                "                            ]\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"DeviceName\": \"温湿度1\",\n" +
                "                            \"BackColor\": \"\",\n" +
                "                            \"FontColor\": \"\",\n" +
                "                            \"ParaInfo\": [\n" +
                "                                {\n" +
                "                                    \"ParaName\": {\n" +
                "                                        \"Name\": \"温度\",\n" +
                "                                        \"BackColor\": \"\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    },\n" +
                "                                    \"ParaValue\": {\n" +
                "                                        \"Name\": \"22.5℃\",\n" +
                "                                        \"BackColor\": \"\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    },\n" +
                "                                    \"ParaState\": {\n" +
                "                                        \"Name\": \"正常\",\n" +
                "                                        \"BackColor\": \"\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    }\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"ParaName\": {\n" +
                "                                        \"Name\": \"温度\",\n" +
                "                                        \"BackColor\": \"\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    },\n" +
                "                                    \"ParaValue\": {\n" +
                "                                        \"Name\": \"22.5℃\",\n" +
                "                                        \"BackColor\": \"\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    },\n" +
                "                                    \"ParaState\": {\n" +
                "                                        \"Name\": \"正常\",\n" +
                "                                        \"BackColor\": \"\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    }\n" +
                "                                }\n" +
                "                            ]\n" +
                "                        }\n" +
                "                    ]\n" +
                "                },\n" +
                "                {\n" +
                "                    \"PlaceName\": \"综合机房\",\n" +
                "                    \"PlaceId\": \"18\",\n" +
                "                    \"DeviceInfo\": [\n" +
                "                        {\n" +
                "                            \"DeviceName\": \"空调\",\n" +
                "                            \"BackColor\": \"\",\n" +
                "                            \"FontColor\": \"\",\n" +
                "                            \"ParaInfo\": [\n" +
                "                                {\n" +
                "                                    \"ParaName\": {\n" +
                "                                        \"Name\": \"温度\",\n" +
                "                                        \"BackColor\": \"\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    },\n" +
                "                                    \"ParaValue\": {\n" +
                "                                        \"Name\": \"22.5℃\",\n" +
                "                                        \"BackColor\": \"\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    },\n" +
                "                                    \"ParaState\": {\n" +
                "                                        \"Name\": \"正常\",\n" +
                "                                        \"BackColor\": \"\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    }\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"ParaName\": {\n" +
                "                                        \"Name\": \"温度\",\n" +
                "                                        \"BackColor\": \"#948A54\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    },\n" +
                "                                    \"ParaValue\": {\n" +
                "                                        \"Name\": \"22.5℃\",\n" +
                "                                        \"BackColor\": \"#948A54\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    },\n" +
                "                                    \"ParaState\": {\n" +
                "                                        \"Name\": \"正常\",\n" +
                "                                        \"BackColor\": \"#948A54\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    }\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"ParaName\": {\n" +
                "                                        \"Name\": \"温度\",\n" +
                "                                        \"BackColor\": \"\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    },\n" +
                "                                    \"ParaValue\": {\n" +
                "                                        \"Name\": \"22.5℃\",\n" +
                "                                        \"BackColor\": \"\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    },\n" +
                "                                    \"ParaState\": {\n" +
                "                                        \"Name\": \"正常\",\n" +
                "                                        \"BackColor\": \"\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    }\n" +
                "                                }\n" +
                "                            ]\n" +
                "                        },\n" +
                "                        {\n" +
                "                            \"DeviceName\": \"温湿度1\",\n" +
                "                            \"BackColor\": \"\",\n" +
                "                            \"FontColor\": \"\",\n" +
                "                            \"ParaInfo\": [\n" +
                "                                {\n" +
                "                                    \"ParaName\": {\n" +
                "                                        \"Name\": \"温度\",\n" +
                "                                        \"BackColor\": \"#948A54\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    },\n" +
                "                                    \"ParaValue\": {\n" +
                "                                        \"Name\": \"22.5℃\",\n" +
                "                                        \"BackColor\": \"#948A54\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    },\n" +
                "                                    \"ParaState\": {\n" +
                "                                        \"Name\": \"正常\",\n" +
                "                                        \"BackColor\": \"#948A54\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    }\n" +
                "                                },\n" +
                "                                {\n" +
                "                                    \"ParaName\": {\n" +
                "                                        \"Name\": \"温度\",\n" +
                "                                        \"BackColor\": \"\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    },\n" +
                "                                    \"ParaValue\": {\n" +
                "                                        \"Name\": \"22.5℃\",\n" +
                "                                        \"BackColor\": \"\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    },\n" +
                "                                    \"ParaState\": {\n" +
                "                                        \"Name\": \"正常\",\n" +
                "                                        \"BackColor\": \"\",\n" +
                "                                        \"FontColor\": \"\"\n" +
                "                                    }\n" +
                "                                }\n" +
                "                            ]\n" +
                "                        }\n" +
                "                    ]\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        \"OperatePart\": {\n" +
                "            \"OperateButton\": {\n" +
                "                \"OperateName\": \"确认收到\",\n" +
                "                \"OperateTypeID\": 1\n" +
                "            }\n" +
                "        },\n" +
                "        \"RemarkPart\": {\n" +
                "            \"Remark\": \"请确认报警好即使处理相关故障</br></br>您点击“确认报警”按钮后15分钟内将不会针对此条报警再做任何提醒</br></br>如果3分钟内您或其他任何负责人员均未处理此条报警，系统将启动短信频台进行报警\"\n" +
                "        }\n" +
                "    }\n" +
                "}";


        ResultVO<Data> infoData = JSON.parseObject(json, new TypeReference<ResultVO<Data>>(){});

        return infoData.getData();
    }

    public static void main(String[] args){
        getInfoData();
    }
}
