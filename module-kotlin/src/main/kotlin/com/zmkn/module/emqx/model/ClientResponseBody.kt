package com.zmkn.module.emqx.model

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientResponseBody(
    @SerialName("awaiting_rel_cnt")
    @param:JsonProperty("awaiting_rel_cnt")
    val awaitingRelCnt: Int,
    @SerialName("awaiting_rel_max")
    @param:JsonProperty("awaiting_rel_max")
    val awaitingRelMax: Int,
    @SerialName("clean_start")
    @param:JsonProperty("clean_start")
    val cleanStart: Boolean,
    @SerialName("clientid")
    @param:JsonProperty("clientid")
    val clientId: String,
    val connected: Boolean,
    @SerialName("connected_at")
    @param:JsonProperty("connected_at")
    val connectedAt: String,
    @SerialName("created_at")
    @param:JsonProperty("created_at")
    val createdAt: String,
    @SerialName("disconnected_at")
    @param:JsonProperty("disconnected_at")
    val disconnectedAt: String? = null,
    @SerialName("expiry_interval")
    @param:JsonProperty("expiry_interval")
    val expiryInterval: Int,
    @SerialName("heap_size")
    @param:JsonProperty("heap_size")
    val heapSize: Int,
    @SerialName("inflight_cnt")
    @param:JsonProperty("inflight_cnt")
    val inflightCnt: Int,
    @SerialName("inflight_max")
    @param:JsonProperty("inflight_max")
    val inflightMax: Int,
    @SerialName("ip_address")
    @param:JsonProperty("ip_address")
    val ipAddress: String,
    @SerialName("is_bridge")
    @param:JsonProperty("is_bridge")
    val isBridge: Boolean,
    @SerialName("is_expired")
    @param:JsonProperty("is_expired")
    val isExpired: Boolean,
    val keepalive: Int,
    @SerialName("mailbox_len")
    @param:JsonProperty("mailbox_len")
    val mailboxLen: Int,
    @SerialName("mqueue_dropped")
    @param:JsonProperty("mqueue_dropped")
    val mqueueDropped: Int,
    @SerialName("mqueue_len")
    @param:JsonProperty("mqueue_len")
    val mqueueLen: Int,
    @SerialName("mqueue_max")
    @param:JsonProperty("mqueue_max")
    val mqueueMax: Int,
    val node: String,
    val port: Int,
    @SerialName("proto_name")
    @param:JsonProperty("proto_name")
    val protoName: String,
    @SerialName("proto_ver")
    @param:JsonProperty("proto_ver")
    val protoVer: Int,
    @SerialName("recv_cnt")
    @param:JsonProperty("recv_cnt")
    val recvCnt: Int,
    @SerialName("recv_msg")
    @param:JsonProperty("recv_msg")
    val recvMsg: Int,
    @SerialName("recv_msg.dropped")
    @param:JsonProperty("recv_msg.dropped")
    val recvMsgDropped: Int,
    @SerialName("recv_msg.dropped.await_pubrel_timeout")
    @param:JsonProperty("recv_msg.dropped.await_pubrel_timeout")
    val recvMsgDroppedAwaitPubrelTimeout: Int,
    @SerialName("recv_msg.qos0")
    @param:JsonProperty("recv_msg.qos0")
    val recvMsgQos0: Int,
    @SerialName("recv_msg.qos1")
    @param:JsonProperty("recv_msg.qos1")
    val recvMsgQos1: Int,
    @SerialName("recv_msg.qos2")
    @param:JsonProperty("recv_msg.qos2")
    val recvMsgQos2: Int,
    @SerialName("recv_oct")
    @param:JsonProperty("recv_oct")
    val recvOct: Int,
    @SerialName("recv_pkt")
    @param:JsonProperty("recv_pkt")
    val recvPkt: Int,
    val reductions: Int,
    @SerialName("send_cnt")
    @param:JsonProperty("send_cnt")
    val sendCnt: Int,
    @SerialName("send_msg")
    @param:JsonProperty("send_msg")
    val sendMsg: Int,
    @SerialName("send_msg.dropped")
    @param:JsonProperty("send_msg.dropped")
    val sendMsgDropped: Int,
    @SerialName("send_msg.dropped.expired")
    @param:JsonProperty("send_msg.dropped.expired")
    val sendMsgDroppedExpired: Int,
    @SerialName("send_msg.dropped.queue_full")
    @param:JsonProperty("send_msg.dropped.queue_full")
    val sendMsgDroppedQueueFull: Int,
    @SerialName("send_msg.dropped.too_large")
    @param:JsonProperty("send_msg.dropped.too_large")
    val sendMsgDroppedTooLarge: Int,
    @SerialName("send_msg.qos0")
    @param:JsonProperty("send_msg.qos0")
    val sendMsgQos0: Int,
    @SerialName("send_msg.qos1")
    @param:JsonProperty("send_msg.qos1")
    val sendMsgQos1: Int,
    @SerialName("send_msg.qos2")
    @param:JsonProperty("send_msg.qos2")
    val sendMsgQos2: Int,
    @SerialName("send_oct")
    @param:JsonProperty("send_oct")
    val sendOct: Int,
    @SerialName("send_pkt")
    @param:JsonProperty("send_pkt")
    val sendPkt: Int,
    @SerialName("subscriptions_cnt")
    @param:JsonProperty("subscriptions_cnt")
    val subscriptionsCnt: Int,
    @SerialName("subscriptions_max")
    @param:JsonProperty("subscriptions_max")
    val subscriptionsMax: Int? = null,
    val username: String,
    @SerialName("mountpoint")
    @param:JsonProperty("mountpoint")
    val mountPoint: Int? = null,
    val durable: Boolean,
    @SerialName("n_streams")
    @param:JsonProperty("n_streams")
    val nStreams: Int? = null,
    @SerialName("seqno_q1_comm")
    @param:JsonProperty("seqno_q1_comm")
    val seqnoQ1Comm: Int? = null,
    @SerialName("seqno_q1_dup")
    @param:JsonProperty("seqno_q1_dup")
    val seqnoQ1Dup: Int? = null,
    @SerialName("seqno_q1_next")
    @param:JsonProperty("seqno_q1_next")
    val seqnoQ1Next: Int? = null,
    @SerialName("seqno_q2_comm")
    @param:JsonProperty("seqno_q2_comm")
    val seqnoQ2Comm: Int? = null,
    @SerialName("seqno_q2_dup")
    @param:JsonProperty("seqno_q2_dup")
    val seqnoQ2Dup: Int? = null,
    @SerialName("seqno_q2_rec")
    @param:JsonProperty("seqno_q2_rec")
    val seqnoQ2Rec: Int? = null,
    @SerialName("seqno_q2_next")
    @param:JsonProperty("seqno_q2_next")
    val seqnoQ2Next: Int? = null,
)
