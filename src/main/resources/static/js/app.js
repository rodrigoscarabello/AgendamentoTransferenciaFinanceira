class Card extends React.Component {
    render() {

        let title = null;
        if(this.props.title){
            title = <span className ="card-title purple-text text-darken-3">{this.props.title}</span>
        }
        let cardAction = null;
        if(this.props.cardAction) {
            cardAction = (
                <div className="card-action right-align">
                    {this.props.cardAction}
                </div>
            )
        }
        return (
            <div className ="card">
                <div className ="card-content">
                    {title}
                    {this.props.children}
                </div>
                {cardAction}
            </div>
        )
    }
}

class Input extends React.Component {
    render() {
        let className = null;
        if(this.props.className)
            className = this.props.className;
        return (
            <div className="input-field col s6 l4">
                <input id={this.props.id} name={this.props.id} type="text" className={className}/>
                <label htmlFor={this.props.id}>{this.props.label}</label>
            </div>
        )
    }
}

class Select extends React.Component {
    componentDidMount() {
        this.$el = $(this.el);
        this.$el.material_select();
    }

    render() {
        return (
            <div className="input-field col s6 l4">
                <select id={this.props.id} name={this.props.id} value="" ref={el => this.el = el}>
                    {this.props.children}
                </select>
                <label>{this.props.label}</label>
            </div>
        );
    }
}

class DivSelect extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            values: undefined
        }
    }

    getTransferTypes = () => {
        fetch("/listTransferTypes")
            .then(response => response.json())
            .then(json => {
                this.setState({
                    values: json
                });
            })
    };

    componentDidMount() {
        this.getTransferTypes();
    };

    render() {
        if(!this.state.values) {
            return null;
        }else {
            var options = [];
            this.state.values.forEach(function(val) {
                options.push(<option value={val} key={val.toString()}>{val}</option>);
            });
            return (
                <Select id={this.props.id} label={this.props.label}>
                    <option value="" disabled>Escolha uma opção</option>
                    {options}
                </Select>
            )
        }
    }
}

class FinancialTransferRegisteredRow extends React.Component {
    render() {
        return (
            <tr>
                <td>{this.props.transfer.originAccount}</td>
                <td>{this.props.transfer.destinationAccount}</td>
                <td>R$ {this.props.transfer.value}</td>
                <td>R$ {this.props.transfer.fee}</td>
                <td>{this.props.transfer.scheduleDate.dayOfMonth}/{this.props.transfer.scheduleDate.monthValue}/{this.props.transfer.scheduleDate.year}</td>
                <td>{this.props.transfer.transferType}</td>
            </tr>
        )
    }
}

class FinancialTransferRegisteredTable extends React.Component {
    render() {
        var rows = [];
        if(this.props.transfers.length == 0) {
            rows.push(<tr key="0"><td colSpan="6" className="center-align">Nenhum agendamento cadastrado</td></tr>);
        }else {
            this.props.transfers.forEach(function (transfer) {
                rows.push(<FinancialTransferRegisteredRow transfer={transfer} key={transfer.id}/>);
            });
        }
        return (
            <Card title="Agendamentos cadastrados">
                <table className ="highlight">
                    <thead>
                    <tr>
                        <th>Conta de origem</th>
                        <th>Conta de destino</th>
                        <th>Valor</th>
                        <th>Taxa</th>
                        <th>Data da transferência</th>
                        <th>Tipo de transferência</th>
                    </tr>
                    </thead>
                    <tbody>{rows}</tbody>
                </table>
            </Card>
        );
    }
}

class ScheduleFinancialTransfer extends React.Component {
    handleSubmit = (event) => {
        event.preventDefault();
    };

    handleClick = () => {
        var _this = this;
        var url = "/schedule?";
        $.each($('form').serializeArray(), function(index, obj) {
            var val = obj.value;
            url += '&' + obj.name + '=';
            if(obj.name == 'value')
                val = val.replace(/\./g,'').replace(',','.');
            url += val;
        });
        fetch(url)
            .then(response => response.json())
            .then(json => {
                if(json.success){
                    Materialize.toast(json.message, 2500, 'green')
                    $('form').trigger('reset');
                    _this.props.onSuccess();
                    $.each($('form').serializeArray(), function(index, obj) {
                        $('#'+obj.name).removeClass('validate invalid');
                        $("label[for='"+obj.name+"']").removeAttr('data-error');
                    });
                }else{
                    if(json.inputMessages.length > 0){
                        $.each(json.inputMessages, function(index, obj) {
                            $('#'+obj.inputName).addClass('validate invalid');
                            $("label[for='"+obj.inputName+"']").attr('data-error',obj.message);
                        });
                    }else if(json.message)
                        Materialize.toast(json.message, 2500, 'red')
                }
            });
    };

    render() {
        const cardAction = (
            <div>
                <a className="waves-effect waves-light btn cyan accent-4" onClick={this.handleClick}><i className="material-icons left">check</i>Agendar</a>
            </div>
        )
        return (
            <Card cardAction={cardAction}>
                <div className="row">
                    <form onSubmit={this.handleSubmit}>
                        <Input id="originAccount" label="Conta de origem" className="account" />
                        <Input id="destinationAccount" label="Conta de destino" className="account"/>
                        <Input id="scheduleDate" label="Data da transferência" className="datepicker"/>
                        <Input id="value" label="Valor"/>
                        <DivSelect id="transferType" label="Tipo de transferência"/>
                    </form>
                </div>
            </Card>
        )
    }
}

class Main extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            transfers: []
        };
    };

    getTransfer = () => {
        fetch("/listSchedules")
            .then(response => response.json())
            .then(json => {
                this.setState({
                    transfers: json
                });
            })
    };

    componentDidMount() {
        this.getTransfer();
    };

    onSuccess = () => {
        this.getTransfer();
    }

    render() {
        return (
            <main>
                <div className="container">
                    <div className="row">
                        <div className="col s12">
                            <div className="section">
                                <h4 className="center-align purple-text text-darken-3">Sistema de agendamento de transferências financeiras</h4>
                                <ScheduleFinancialTransfer onSuccess={this.onSuccess}/>
                                <FinancialTransferRegisteredTable transfers={this.state.transfers}/>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        )
    }
}

ReactDOM.render(
    <Main />,
    document.getElementById('root')
);

$(document).ready(function() {
    $('#value').mask("#.##0,00", {reverse: true});
    $('.account').mask('AAAAA-A');
    $('.datepicker').pickadate({
        today: '',
        clear: 'Limpar',
        close: 'Ok',
        closeOnSelect: true,
        format: 'dd/mm/yyyy',
        formatSubmit: 'dd/mm/yyyy',
        hiddenName: true,
        min: true,
        monthsFull: ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'],
        monthsShort: ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez'],
        weekdaysFull: ['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado'],
        weekdaysShort: ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb']
    });
});