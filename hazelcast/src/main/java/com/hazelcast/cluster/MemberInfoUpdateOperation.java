/*
 * Copyright (c) 2008-2012, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.cluster;

import com.hazelcast.nio.Address;
import com.hazelcast.nio.Connection;
import com.hazelcast.util.Clock;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class MemberInfoUpdateOperation extends AbstractClusterOperation implements JoinOperation {

    private static final long serialVersionUID = -2311579721761844861L;

    private Collection<MemberInfo> memberInfos;

    private long masterTime = Clock.currentTimeMillis();

    private boolean sendResponse = false;

    public MemberInfoUpdateOperation() {
        memberInfos = new ArrayList<MemberInfo>();
    }

    public MemberInfoUpdateOperation(Collection<MemberInfo> members, long masterTime) {
        this.masterTime = masterTime;
        this.memberInfos = members;
    }

    public MemberInfoUpdateOperation(Collection<MemberInfo> memberInfos, long masterTime, boolean sendResponse) {
        this.masterTime = masterTime;
        this.memberInfos = memberInfos;
        this.sendResponse = sendResponse;
    }

    public void run() {
        if (isValid()) {
            final ClusterService clusterService = getService();
            clusterService.setMasterTime(masterTime);
            clusterService.updateMembers(memberInfos);
        }
    }

    protected boolean isValid() {
        final ClusterService clusterService = getService();
        final Connection conn = getConnection();
        final Address masterAddress = conn != null ? conn.getEndPoint() : null;
        return conn == null ||  // which means this is a local call.
                               (masterAddress != null && masterAddress.equals(clusterService.getMasterAddress()));
    }

    @Override
    public boolean returnsResponse() {
        return sendResponse;
    }

//    @Override
//    public Object getResponse() {
//        return Boolean.TRUE;
//    }

    @Override
    protected void readInternal(DataInput in) throws IOException {
        masterTime = in.readLong();
        int size = in.readInt();
        memberInfos = new ArrayList<MemberInfo>(size);
        while (size-- > 0) {
            MemberInfo memberInfo = new MemberInfo();
            memberInfo.readData(in);
            memberInfos.add(memberInfo);
        }
    }

    @Override
    protected void writeInternal(DataOutput out) throws IOException {
        out.writeLong(masterTime);
        out.writeInt(memberInfos.size());
        for (MemberInfo memberInfo : memberInfos) {
            memberInfo.writeData(out);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("MembersUpdateCall {\n");
        for (MemberInfo address : memberInfos) {
            sb.append(address).append('\n');
        }
        sb.append('}');
        return sb.toString();
    }
}
