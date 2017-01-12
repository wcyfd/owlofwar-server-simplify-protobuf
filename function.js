/**
 * 购买需要花费的银币数量
 * @param type 0白 1蓝 2橙 3紫
 * @param count 购买次数
 * @returns
 */
function getCostMoney(type, count) {
	var baseCount = 0;
	if (type == 0) {
		// 白
		baseCount = 20;
	} else if (type == 1) {
		// 蓝
		baseCount = 120;
	} else if (type == 2) {
		// 橙
		baseCount = 600;
	} else if (type == 3) {
		// 紫
		baseCount = 2700;
	}
	return Math.ceil(baseCount * Math.pow(1.4, (count - 1)));
}
