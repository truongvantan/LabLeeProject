package com.lablee.admin.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PaginationCommon {
	
	// cài đặt phân trang
		/*
			+ Hiển thị tối đa 10 link số.
			+ Ví dụ như hiện tại là trang số 1 thì sẽ hiển thị các link có số là 1 đến 10.
			+ Giả sử hiện tại là trang số 2 thì sẽ hiển thị các link có số từ 1 đến 10.
			+ Lấy số trang hiện tại làm chuẩn, tiếp theo sẽ đánh số trang lùi về trước 4 số, 
				và tiến về sau là 5 số so với số trang hiện tại.
			+ Nếu trang hiện tại là trang số 5 thì ta sẽ chọn 10 link số là 1 2 3 4 5 6 7 8 9 10.
			+ Nếu trang hiện tại là trang số 6 thì ta sẽ chọn 10 link số là 2 3 4 5 6 7 8 9 10 11.
			+ Nếu trang hiện tại mà là trang số 3 thì ta sẽ chọn 10 link số là 1 2 3 4 5 6 7 8 9 10.
			+ Nếu mà lùi về trước không đủ 4 số thì sẽ bù số lượng thiếu cho các số trang ở phía sau.
		
		=> Có 5 tình huống xảy ra:
			+ Tình huống 1: Nếu tổng số trang <= 10 (totalPageNumber <= 10).
				Thì luôn luôn in ra tất cả các số trang.
				
			+ Tình huống 2: Nếu tổng số trang lớn hơn 10 và trang hiện tại là trang cuối cùng 
				thì sẽ in 10 số cuối cùng (so với biến totalPageNumber).
				- Ví dụ totalPageNumber = 100 thì sẽ in 91 92 93 94 95 96 97 98 99 100.
				
			+ Tình huống 3: Nếu tổng số trang lớn hơn 10, và currentPageNumer <= 4
				thì luôn luôn in ra là 1 2 3 4 5 6 7 8 9 10.
				
			+ Tình huống 4: Nếu tổng số trang lớn hơn 10, và currentPageNumer >= (totalPageNumber - 5)
				thì luôn luôn in ra 10 số cuối cùng so với totalPageNumber.
				- Ví dụ: totalPageNumber = 100, currentPageNumer = 95 hoặc (96, 97, 98, 99, 100)
					thì sẽ luôn luôn in ra là 91 92 93 94 95 96 97 98 99 100.
				=> Tình huống 4 sẽ bao gồm luôn tình huống 2
				
			+ Tình huống 5: Nếu tổng số trang lớn hơn 10, và currentPageNumer >= 5 và currentPageNumer <= (totalPageNumber - 5)
				thì sẽ in 10 số liên tiếp, tính từ số currentPageNumer - 3 cho đến currentPageNumer + 6.
				Ví dụ như totalPageNumber = 100, currentPageNumer = 10
					thì ta sẽ in ra dãy số là: 7 8 9 10 11 12 13 14 15 16
		*/

	public List<Integer> getListPageNumbers(int totalPageNumber, int currentPageNumber) {
		List<Integer> pageNumbers = new ArrayList<>();

		if (totalPageNumber <= 10) { // Situation 1
			for (int i = 1; i <= totalPageNumber; i++) {
				pageNumbers.add(i);
			}

		} else if (currentPageNumber <= 5) { // Situation 3
			for (int i = 1; i <= 10; i++) {
				pageNumbers.add(i);
			}

		} else if (currentPageNumber >= (totalPageNumber - 5)) { // Situation 4
			for (int i = totalPageNumber - 9; i <= totalPageNumber; i++) {
				pageNumbers.add(i);
			}

		} else { // Situation 5
			for (int i = currentPageNumber - 3; i <= currentPageNumber + 6; i++) {
				pageNumbers.add(i);
			}
		}

		return pageNumbers;
	}

}
