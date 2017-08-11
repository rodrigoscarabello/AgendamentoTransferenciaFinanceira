class Card extends React.Component {
    render() {

        let title = null;
        if(this.props.title){
            title = <span className ="card-title">{this.props.title}</span>
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

class Button extends React.Component {
    render() {
        return (
            <a className="waves-effect waves-light btn cyan"><i className="material-icons left">{this.props.icon}</i>{this.props.text}</a>
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
                <input id={this.props.id} type="text" className={className}/>
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

    componentWillUnmount() {
        this.$el.chosen('destroy');
    }

    render() {
        return (
            <div className="input-field col s6 l4">
                <select id={this.props.id} ref={el => this.el = el}>
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
        this.state ={
            values: undefined
        }
    }

    _getTransferTypes = () => {
        fetch("/listTransferTypes")
            .then(response => response.json())
            .then(json => {
                this.setState({
                    values: json
                });
            })
    };

    componentDidMount() {
        this._getTransferTypes();
    };

    render() {
        if(!this.state.values) {
            return null;
        }else {
            var options = [];
            this.state.values.forEach(function(val) {
                options.push(<option value={val}>{val}</option>);
            });
            return (
                <Select id={this.props.id} label={this.props.label}>
                    <option value="" disabled selected>Escolha uma opção</option>
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
                <td>{this.props.transfer.value}</td>
                <td>{this.props.transfer.fee}</td>
                <td>{this.props.transfer.scheduleDate}</td>
                <td>{this.props.transfer.transferType}</td>
            </tr>
        )
    }
}

class FinancialTransferRegisteredTable extends React.Component {
    render() {
        var rows = [];
        this.props.transfers.forEach(function(transfer) {
            rows.push(<FinancialTransferRegisteredRow transfer={transfer} key={transfer.id} />);
        });
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
    render() {
        const cardAction = (
            <div>
                <a className="btn-flat grey-text text-lighten-1">Cancelar</a>
                <Button icon="check" text="Agendar"/>
            </div>
        )
        return (
            <Card cardAction={cardAction}>
                <div className="row">
                    <Input id="originAccount" label="Conta de origem"/>
                    <Input id="destinationAccount" label="Conta de destino"/>
                    <Input id="scheduleDate" label="Data da transferência" className="datepicker"/>
                    <Input id="value" label="Valor"/>
                    <DivSelect id="transferType" label="Tipo de transferência"/>
                    <div className="input-field col s6 l4">
                        <input id="fee" type="text"/>
                        <label htmlFor="fee">Taxa</label>
                    </div>
                </div>
            </Card>
        )
    }
}

class Main extends React.Component {
    render() {
        return (
            <main>
                <div className="container">
                    <div className="row">
                        <div className="col s12">
                            <div className="section">
                                <Button icon="add" text="Agendar transferência"/>
                                <ScheduleFinancialTransfer/>
                                <FinancialTransferRegisteredTable transfers={this.props.transfers} />
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        )
    }
}

var TRANSFERS = [
    {id: 1, originAccount: '12345-6', destinationAccount: '78945-6', value: 'R$ 7,00', fee: 'R$ 1,50', scheduleDate: '11/08/2017', transferType: 'A'},
    {id: 2, originAccount: '12345-6', destinationAccount: '78945-6', value: 'R$ 7,00', fee: 'R$ 1,50', scheduleDate: '11/08/2017', transferType: 'A'},
    {id: 3, originAccount: '12345-6', destinationAccount: '78945-6', value: 'R$ 7,00', fee: 'R$ 1,50', scheduleDate: '11/08/2017', transferType: 'A'},
    {id: 4, originAccount: '12345-6', destinationAccount: '78945-6', value: 'R$ 7,00', fee: 'R$ 1,50', scheduleDate: '11/08/2017', transferType: 'A'}
];

ReactDOM.render(
    <Main transfers={TRANSFERS} />,
    document.getElementById('root')
);

$(document).ready(function() {
    $('.datepicker').pickadate({
        today: '',
        clear: 'Limpar',
        close: 'Ok',
        closeOnSelect: true,
        format: 'dd/mm/yyyy',
        formatSubmit: 'yyyy/mm/dd',
        hiddenName: true,
        min: true
    });
});