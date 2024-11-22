import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import '../style.css';

function CurrentInjectionStatus() {
    const [data, setData] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 9; // 페이지당 9개의 기기 표시

    useEffect(() => {
        fetch("http://localhost:8080/machines/dosage")
            .then(response => response.json())
            .then(data => {
                const filteredData = data
                    .filter(item => item.machineState === 1)
                    .sort((a, b) => {
                        const timeA = a.totalTime.split(':').reduce((acc, time) => (60 * acc) + +time);
                        const timeB = b.totalTime.split(':').reduce((acc, time) => (60 * acc) + +time);
                        return timeB - timeA;
                    });
                setData(filteredData);
            })
            .catch(error => console.error("Error fetching data:", error));
    }, []);

    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentItems = data.slice(indexOfFirstItem, indexOfLastItem);

    const totalPages = Math.ceil(data.length / itemsPerPage);

    const handlePageChange = (page) => {
        setCurrentPage(page);
    };

    return (
        <div className="injection-status">
            <header>
                <h2 style={{fontSize:'42px'}}>현재 투여 확인</h2>
            </header>
            <div className="grid" style={{fontSize:'28px' }}>
                {currentItems.map((item, index) => (
                    <div key={item.id || index} className="machine-card">
                        <p>기기번호 : {item.id}</p>
                        <p>경과시간 : {item.totalTime}</p>
                        <p>투약속도 : {item.dosageSpeed} m/s</p>
                        <p>투여량 : {item.dosageValue} ml</p>
                    </div>
                ))}
            </div>
            <div className="pagination">
                {Array.from({ length: totalPages }, (_, index) => (
                    <button
                        key={index + 1}
                        onClick={() => handlePageChange(index + 1)}
                        className={currentPage === index + 1 ? 'active' : ''}
                    >
                        {index + 1}
                    </button>
                ))}
            </div>
        </div>
    );
}

export default CurrentInjectionStatus;
