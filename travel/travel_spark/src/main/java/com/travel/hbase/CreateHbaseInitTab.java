package com.travel.hbase;

import com.travel.common.Constants;
import com.travel.utils.HbaseTools;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class CreateHbaseInitTab {
    public static void main(String[] args) throws IOException {
        Connection hbaseConn = HbaseTools.getHbaseConn();
        Admin admin = hbaseConn.getAdmin();
        String[] tableNames = {"order_info", "renter_info", "driver_info", "opt_alliance_business"};
        byte[][] bytesNum = new byte[8][];

        for(int i=0;i<8;i++){
            String leftPad = StringUtils.leftPad(i + "", 4, "0");
            bytesNum[i] = Bytes.toBytes(leftPad+"|");
        }
        for (String tableName :tableNames){
            HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(Constants.DEFAULT_DB_FAMILY);
            hTableDescriptor.addFamily(hColumnDescriptor);
            admin.createTable(hTableDescriptor,bytesNum);
        }
        admin.close();
        hbaseConn.close();
    }
}
