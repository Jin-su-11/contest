import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import '../style.css';

function LastInjectionStatus() {
    const [data, setData] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 9;

    useEffect(() => {
        fetch("http://localhost:8080/machines/dosage")
            .then(response => response.json())
            .then(data => {
                const filteredData = data
                    .filter(item => item.machineState === 0)
                    .sort((a, b) => new Date(b.dosageCreatedAt) - new Date(a.dosageCreatedAt));
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
                <h2 style={{fontSize:'42px'}}>지난 투여 확인</h2>
            </header>
            <div className="grid" style={{fontSize:'28px' }}>
                {currentItems.map((item, index) => (
                    <div key={item.id || index} className="machine-card">
                        <p>기기번호 : {item.id}</p>
                        <p>날짜 : {item.dosageCreatedAt}</p>
                        <p>투여시간 : {item.totalTime}</p>
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

export default LastInjectionStatus;
