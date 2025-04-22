import React from "react";

const Depot: React.FC = () => {
    return (
        <div id="auth">

            <div className="row h-100">
                <div className="col-lg-5 col-12">
                    <div id="auth-left">
                        <div className="auth-logo">
                            <h1 className="audiowide"> <a href="home"><img src="/img/logo_final.svg" alt="Logo"/> Cryptonic </a> </h1>
                        </div>
                        <h1 className="auth-subtitle">Depot</h1>

                        <form action="index.html">
                            <div className="form-group position-relative has-icon-left mb-4">
                                <input type="number" min="0.01" step="0.01" className="form-control form-control-xl"
                                       placeholder="Montant"/>
                                <div className="form-control-icon">
                                    <i className="bi bi-cash"></i>
                                </div>
                            </div>
                            <button className="btn btn-primary btn-block btn-lg shadow-lg mt-5">Déposer</button>
                        </form>
                    </div>
                </div>
                <div className="col-lg-7 d-none d-lg-block">
                    <div id="auth-right">

                    </div>
                </div>
            </div>

        </div>
    );
};

export default Depot;