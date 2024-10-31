import React, { useEffect, useState } from 'react';

function DataDisplay() {
    const [data, setData] = useState([]);

    // 백엔드에서 데이터 불러오기
    useEffect(() => {
        fetch("http://localhost:8080/api/data")  // API 엔드포인트에 요청
            .then(response => response.json())
            .then(data => setData(data))
            .catch(error => console.error("Error fetching data:", error));
    }, []);

    return (
        <div className="data-display">
            <h1>Arduino Data</h1>
            <ul>
                {data.map((item, index) => (
                    <li key={index}>
                        Device ID: {item.deviceId} - Total Injected Value: {item.injectTotalValue} - Dropping Speed: {item.droppingSpeed}
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default DataDisplay;
