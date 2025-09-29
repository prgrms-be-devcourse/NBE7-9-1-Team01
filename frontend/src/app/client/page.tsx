"use client";

import { useEffect, useState } from "react";

export default function Client() {
  const [products, setProducts] = useState([]);
  const [cart, setCart] = useState<
    { id: number; name: string; price: number; count: number }[]
  >([]);
  const [selectedPayment, setSelectedPayment] = useState<string | null>(null);

  // 백엔드 서버에 저장되어 있는 상품 목록 가져오기 
  useEffect(() => {
    fetch("http://localhost:8080/api/products") 
      .then(res => res.json())
      .then(json => setProducts(json.data))
      .catch(err => console.error(err));
  }, []);

  // 상품 "추가" 버튼 클릭 시, 장바구니에 추가
  const handleAddToCart = (product : any) => {
    setCart(prevCart => {
      const existing = prevCart.find(item => item.id === product.id);

      if (existing) {
        return prevCart.map(item =>
          item.id === product.id ? { ...item, count: item.count + 1 } : item
        );
      } else {
        return [...prevCart, { id: product.id, name: product.name, price: product.price, count: 1 }];
      }
    });
  };

  // 상품 "삭제" 버튼 클릭 시, 장바구니에서 삭제
  const handleRemoveFromCart = (e : React.MouseEvent<HTMLButtonElement>, productId: number) => {
    e.preventDefault();

    setCart(prevCart =>
      prevCart
        .map(item =>
          item.id === productId ? { ...item, count: item.count - 1 } : item
        )
        .filter(item => item.count > 0)
    );
  };

  // 장바구니에 담은 상품의 총 금액 계산
  const totalPrice = cart.reduce((sum, item) => sum + item.price * item.count, 0);

  // 3개의 결제 수단 중, 1개만 선택할 수 있도록 설정
  const handlePaymentSelect = (method: string) => {
    setSelectedPayment(method);
  };

  const paymentEnumMap: Record<string, string> = {
    "신용카드": "CREDIT_CARD",
    "계좌이체": "BANK_TRANSFER",
    "포인트": "POINTS",
  };

  // 결제
  const handlePay = async (e: any) => {
    e.preventDefault();
    const form = e.target;
    const email = form.email;
    const address = form.address;
    const postcode = form.postcode;

    if (cart.length === 0) {
      alert("상품을 장바구니에 추가해주세요 :)");
      return;
    }

    if(email.value.length === 0) {
      alert("이메일을 입력해주세요 :)");
      email.focus();
      return;
    }

    if(address.value.length === 0) {
      alert("주소를 입력해주세요 :)");
      address.focus();
      return;
    }

    if(postcode.value.length === 0) {
      alert("우편번호를 입력해주세요 :)");
      postcode.focus();
      return;
    }

    if (!selectedPayment) {
      alert("결제 수단을 선택해주세요 :)");
      return;
    }

    try {
      // 각 상품마다 주문 생성
      const orderResponses = await Promise.all(
        cart.map(item =>
          fetch("http://localhost:8080/api/order", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
              email: email.value,
              address: address.value,
              postcode: postcode.value,
              productId: item.id,
              quantity: item.count,
            }),
          }).then(res => res.json())
        )
      );

      // 각 주문에 대해 결제 실행
      await Promise.all(
        orderResponses.map(order =>
          fetch("http://localhost:8080/api/payments", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
              orderId: order.data.orderId,
              paymentMethod: paymentEnumMap[selectedPayment],
            }),
          })
        )
      );

      alert("모든 상품의 결제가 완료되었습니다 ☕️");
    } catch (err) {
      console.error(err);
      alert("결제 중 오류가 발생했습니다.");
    }

  };

  return (
    <div className="container-fluid" style={{ background: '#ddd', minHeight: '100vh', padding: '24px 12px' }}>
      <div className="row justify-content-center m-4">
        <h1 className="text-center">Grids &amp; Circle</h1>
      </div>

      <div className="card" style={{ 
        margin: 'auto', 
        maxWidth: '950px', 
        width: '90%', 
        boxShadow: '0 6px 20px rgba(0,0,0,.19)', 
        borderRadius: '1rem', 
        border: 'transparent',
        background: '#fff'
      }}>
        <div className="row">
          {/* 왼쪽: 상품 리스트 */}
          <div className="col-md-7 mt-4 d-flex flex-column align-items-start p-5 pt-3">
            <h5 className="flex-grow-0"><b>상품 목록</b></h5>
            <ul className="list-group">
              {(products as Array<{
                id: number;
                name: string;
                price: number;
                description: string;
                category: string;
                createDate: string;
              }>).map(product => (
                <li className="list-group-item d-flex align-items-center mt-4" key={product.id}>
                  <div className="col-2 me-3">
                    <img className="img-fluid" src="https://i.imgur.com/HKOFQYa.jpeg" alt="상품"/>
                  </div>
                  <div className="col-5 me-6">
                  <div className="row text-muted">{product.category}</div>
                    <div className="row">{product.name}</div>
                  </div>
                  <div className="col me-2 text-end">{product.price}원</div>
                  <div className="col text-end">
                    <button className="btn btn-outline-dark btn-sm" onClick={() => handleAddToCart(product)}>추가</button>
                  </div>
                </li>
              ))}
            </ul>
          </div>

          {/* 오른쪽: Summary */}
          <div className="col-md-5" style={{ 
            background: '#ddd', 
            borderTopRightRadius: '1rem', 
            borderBottomRightRadius: '1rem', 
            padding: '4vh', 
            color: '#414141' 
          }}>
            <div className="d-flex flex-column align-items-center">
              <h5 className="align-self-start mb-4"><b>Summary</b></h5>
              <div style={{ width: '100%', maxWidth: '300px' }}>
                <h6>📌 당일 오후 2시 이후의 주문은 다음날 배송을 시작합니다.</h6>

                <hr style={{ marginTop: '1.25rem' }} />
                <form onSubmit={handlePay}>
                  <ul className="list-group mb-4">
                    {cart.map(item => (
                      <li key={item.id} className="list-group-item d-flex justify-content-between align-items-center">
                        <span className="col-8 me-2">{item.name}</span>
                        <span className="me-1" style={{ minWidth: "auto" }}>{item.count}개</span>
                        <button
                          className="btn btn-outline-dark btn-sm"
                          onClick={(e) => handleRemoveFromCart(e, item.id)}
                        >
                          삭제
                        </button>
                      </li>
                    ))}
                    {cart.length === 0 && (
                      <li className="list-group-item text-center">상품을 추가해주세요 ;)</li>
                    )}
                  </ul>

                  <div className="mb-3">
                    <label htmlFor="email" className="form-label">이메일</label>
                    <input name="email" type="email" className="form-control mb-1" id="email" />
                  </div>
                  <div className="mb-3">
                    <label htmlFor="address" className="form-label">주소</label>
                    <input name="address" type="text" className="form-control mb-1" id="address" />
                  </div>
                  <div className="mb-3">
                    <label htmlFor="postcode" className="form-label">우편번호</label>
                    <input name="postcode" type="text" className="form-control" id="postcode" />
                  </div>

                  <div className="row pt-2 pb-2 border-top">
                    <h5 className="col">총금액</h5>
                    <h5 className="col text-end">{totalPrice.toLocaleString()}원</h5>
                  </div>

                  <div className="mb-2 d-flex gap-2 justify-content-between">
                    {["신용카드", "계좌이체", "포인트"].map((method) => (
                      <button
                        key={method}
                        type="button"
                        className={`btn ${selectedPayment === method ? "btn-dark" : "btn-outline-dark"}`}
                        onClick={() => handlePaymentSelect(method)}
                      >
                        {method}
                      </button>
                      ))}
                  </div>

                  <button className="btn btn-dark col-12" type="submit">결제하기</button>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}