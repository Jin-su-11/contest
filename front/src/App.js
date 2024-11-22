import React from 'react';
import { BrowserRouter as Router, Route, Link, Routes } from 'react-router-dom';
import CurrentInjectionStatus from './contest_front/CurrentInjectionStatus';
import LastInjectionStatus from './contest_front/LastInjectionStatus';

function App() {
    return (
        <Router>
            <div>
                <header>
                    <h1 style={{marginLeft:'40px'}}>동국대 병원 메인병동</h1>
                    <div style={{fontSize:'24px', display: 'flex', justifyContent:'right', alignItems: 'right', marginRight:'40px'}}>
                    <nav>
                        <Link to="/">현재 투여</Link> | <Link to="/last">지난 결과</Link>
                    </nav>
                    </div>
                </header>
                <Routes>
                    <Route path="/" element={<CurrentInjectionStatus />} />
                    <Route path="/last" element={<LastInjectionStatus />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
