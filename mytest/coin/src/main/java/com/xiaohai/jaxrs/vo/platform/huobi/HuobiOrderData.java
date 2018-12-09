package com.xiaohai.jaxrs.vo.platform.huobi;

public class HuobiOrderData {
	// 账户ID
		private long accountId;
		
		// 订单数量
		private String amount;
		
		// 订单撤销时间	
		private long canceledAt;
		
		// 订单创建时间
		private long createdAt;
		
		// 已成交数量
		private String fieldAmoint;
		
		// 已成交总金额
		private String fieldCashAmount;
		
		// 已成交手续费（买入为币，卖出为钱）
		private String fieldFees;
		
		// 最后成交时间
		private long finishedAt;
		
		// 订单ID
		private long id;
		
		// 订单价格
		private String price;
		
		// 订单来源 api
		private String source;
		
		// 订单状态	pre-submitted 准备提交, submitting , submitted 已提交,
		//partial-filled 部分成交, partial-canceled 部分成交撤销, filled 完全成交, canceled 已撤销
		private String state;
		
		// 交易对btcusdt, bchbtc, rcneth ...
		private String symbol;
		
		// 订单类型	submit-cancel：已提交撤单申请 ,buy-market：市价买, sell-market：市价卖, buy-limit：限价买, sell-limit：限价卖
		private String type;

		public long getAccountId() {
			return accountId;
		}

		public void setAccountId(long accountId) {
			this.accountId = accountId;
		}

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

		public long getCanceledAt() {
			return canceledAt;
		}

		public void setCanceledAt(long canceledAt) {
			this.canceledAt = canceledAt;
		}

		public long getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(long createdAt) {
			this.createdAt = createdAt;
		}

		public String getFieldAmoint() {
			return fieldAmoint;
		}

		public void setFieldAmoint(String fieldAmoint) {
			this.fieldAmoint = fieldAmoint;
		}

		public String getFieldCashAmount() {
			return fieldCashAmount;
		}

		public void setFieldCashAmount(String fieldCashAmount) {
			this.fieldCashAmount = fieldCashAmount;
		}

		public String getFieldFees() {
			return fieldFees;
		}

		public void setFieldFees(String fieldFees) {
			this.fieldFees = fieldFees;
		}

		public long getFinishedAt() {
			return finishedAt;
		}

		public void setFinishedAt(long finishedAt) {
			this.finishedAt = finishedAt;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getSymbol() {
			return symbol;
		}

		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
		
}
