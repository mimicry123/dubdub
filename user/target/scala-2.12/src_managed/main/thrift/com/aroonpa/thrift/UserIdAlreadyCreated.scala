/**
 * Generated by Scrooge
 *   version: 17.12.0
 *   rev: 9690ae0f172e5ee330a937f5fb935c5db3e8e6ca
 *   built at: 20171208-172837
 */
package com.aroonpa.thrift

import com.twitter.io.Buf
import com.twitter.scrooge.{
  HasThriftStructCodec3,
  LazyTProtocol,
  TFieldBlob,
  ThriftException,
  ThriftStruct,
  ThriftStructCodec3,
  ThriftStructFieldInfo,
  ThriftStructMetaData,
  ThriftUtil
}
import org.apache.thrift.protocol._
import org.apache.thrift.transport.{TMemoryBuffer, TTransport, TIOStreamTransport}
import java.io.ByteArrayInputStream
import java.nio.ByteBuffer
import java.util.Arrays
import java.util.concurrent.atomic.AtomicInteger
import scala.collection.immutable.{Map => immutable$Map}
import scala.collection.mutable.Builder
import scala.collection.mutable.{
  ArrayBuffer => mutable$ArrayBuffer, Buffer => mutable$Buffer,
  HashMap => mutable$HashMap, HashSet => mutable$HashSet}
import scala.collection.{Map, Set}


object UserIdAlreadyCreated extends ThriftStructCodec3[UserIdAlreadyCreated] {
  val NoPassthroughFields: immutable$Map[Short, TFieldBlob] = immutable$Map.empty[Short, TFieldBlob]
  val Struct = new TStruct("UserIdAlreadyCreated")
  val MessageField = new TField("message", TType.STRING, 1)
  val MessageFieldManifest = implicitly[Manifest[String]]

  /**
   * Field information in declaration order.
   */
  lazy val fieldInfos: scala.List[ThriftStructFieldInfo] = scala.List[ThriftStructFieldInfo](
    new ThriftStructFieldInfo(
      MessageField,
      false,
      false,
      MessageFieldManifest,
      _root_.scala.None,
      _root_.scala.None,
      immutable$Map.empty[String, String],
      immutable$Map.empty[String, String],
      None
    )
  )

  lazy val structAnnotations: immutable$Map[String, String] =
    immutable$Map.empty[String, String]

  /**
   * Checks that all required fields are non-null.
   */
  def validate(_item: UserIdAlreadyCreated): Unit = {
  }

  def withoutPassthroughFields(original: UserIdAlreadyCreated): UserIdAlreadyCreated =
    new UserIdAlreadyCreated(
      message =
        {
          val field = original.message
          field
        }
    )

  override def encode(_item: UserIdAlreadyCreated, _oproto: TProtocol): Unit = {
    _item.write(_oproto)
  }


  override def decode(_iprot: TProtocol): UserIdAlreadyCreated = {
    var message: String = null
    var _passthroughFields: Builder[(Short, TFieldBlob), immutable$Map[Short, TFieldBlob]] = null
    var _done = false

    _iprot.readStructBegin()
    while (!_done) {
      val _field = _iprot.readFieldBegin()
      if (_field.`type` == TType.STOP) {
        _done = true
      } else {
        _field.id match {
          case 1 =>
            _field.`type` match {
              case TType.STRING =>
                message = readMessageValue(_iprot)
              case _actualType =>
                val _expectedType = TType.STRING
                throw new TProtocolException(
                  "Received wrong type for field 'message' (expected=%s, actual=%s).".format(
                    ttypeToString(_expectedType),
                    ttypeToString(_actualType)
                  )
                )
            }
          case _ =>
            if (_passthroughFields == null)
              _passthroughFields = immutable$Map.newBuilder[Short, TFieldBlob]
            _passthroughFields += (_field.id -> TFieldBlob.read(_field, _iprot))
        }
        _iprot.readFieldEnd()
      }
    }
    _iprot.readStructEnd()

    new UserIdAlreadyCreated(
      message,
      if (_passthroughFields == null)
        NoPassthroughFields
      else
        _passthroughFields.result()
    )
  }

  def apply(
    message: String
  ): UserIdAlreadyCreated =
    new UserIdAlreadyCreated(
      message
    )

  def unapply(_item: UserIdAlreadyCreated): _root_.scala.Option[String] = _root_.scala.Some(_item.message)


  @inline private[thrift] def readMessageValue(_iprot: TProtocol): String = {
    _iprot.readString()
  }

  @inline private def writeMessageField(message_item: String, _oprot: TProtocol): Unit = {
    _oprot.writeFieldBegin(MessageField)
    writeMessageValue(message_item, _oprot)
    _oprot.writeFieldEnd()
  }

  @inline private def writeMessageValue(message_item: String, _oprot: TProtocol): Unit = {
    _oprot.writeString(message_item)
  }


}

/**
 * Prefer the companion object's [[com.aroonpa.thrift.UserIdAlreadyCreated.apply]]
 * for construction if you don't need to specify passthrough fields.
 */
class UserIdAlreadyCreated(
    val message: String,
    val _passthroughFields: immutable$Map[Short, TFieldBlob])
  extends ThriftException with com.twitter.finagle.SourcedException with ThriftStruct
  with _root_.scala.Product1[String]
  with HasThriftStructCodec3[UserIdAlreadyCreated]
  with java.io.Serializable
{
  import UserIdAlreadyCreated._
  def this(
    message: String
  ) = this(
    message,
    Map.empty
  )

  def _1 = message


  /**
   * Gets a field value encoded as a binary blob using TCompactProtocol.  If the specified field
   * is present in the passthrough map, that value is returned.  Otherwise, if the specified field
   * is known and not optional and set to None, then the field is serialized and returned.
   */
  def getFieldBlob(_fieldId: Short): _root_.scala.Option[TFieldBlob] = {
    lazy val _buff = new TMemoryBuffer(32)
    lazy val _oprot = new TCompactProtocol(_buff)
    _passthroughFields.get(_fieldId) match {
      case blob: _root_.scala.Some[TFieldBlob] => blob
      case _root_.scala.None => {
        val _fieldOpt: _root_.scala.Option[TField] =
          _fieldId match {
            case 1 =>
              if (message ne null) {
                writeMessageValue(message, _oprot)
                _root_.scala.Some(UserIdAlreadyCreated.MessageField)
              } else {
                _root_.scala.None
              }
            case _ => _root_.scala.None
          }
        _fieldOpt match {
          case _root_.scala.Some(_field) =>
            _root_.scala.Some(TFieldBlob(_field, Buf.ByteArray.Owned(_buff.getArray())))
          case _root_.scala.None =>
            _root_.scala.None
        }
      }
    }
  }

  /**
   * Collects TCompactProtocol-encoded field values according to `getFieldBlob` into a map.
   */
  def getFieldBlobs(ids: TraversableOnce[Short]): immutable$Map[Short, TFieldBlob] =
    (ids flatMap { id => getFieldBlob(id) map { id -> _ } }).toMap

  /**
   * Sets a field using a TCompactProtocol-encoded binary blob.  If the field is a known
   * field, the blob is decoded and the field is set to the decoded value.  If the field
   * is unknown and passthrough fields are enabled, then the blob will be stored in
   * _passthroughFields.
   */
  def setField(_blob: TFieldBlob): UserIdAlreadyCreated = {
    var message: String = this.message
    var _passthroughFields = this._passthroughFields
    _blob.id match {
      case 1 =>
        message = readMessageValue(_blob.read)
      case _ => _passthroughFields += (_blob.id -> _blob)
    }
    new UserIdAlreadyCreated(
      message,
      _passthroughFields
    )
  }

  /**
   * If the specified field is optional, it is set to None.  Otherwise, if the field is
   * known, it is reverted to its default value; if the field is unknown, it is removed
   * from the passthroughFields map, if present.
   */
  def unsetField(_fieldId: Short): UserIdAlreadyCreated = {
    var message: String = this.message

    _fieldId match {
      case 1 =>
        message = null
      case _ =>
    }
    new UserIdAlreadyCreated(
      message,
      _passthroughFields - _fieldId
    )
  }

  /**
   * If the specified field is optional, it is set to None.  Otherwise, if the field is
   * known, it is reverted to its default value; if the field is unknown, it is removed
   * from the passthroughFields map, if present.
   */
  def unsetMessage: UserIdAlreadyCreated = unsetField(1)


  override def write(_oprot: TProtocol): Unit = {
    UserIdAlreadyCreated.validate(this)
    _oprot.writeStructBegin(Struct)
    if (message ne null) writeMessageField(message, _oprot)
    if (_passthroughFields.nonEmpty) {
      _passthroughFields.values.foreach { _.write(_oprot) }
    }
    _oprot.writeFieldStop()
    _oprot.writeStructEnd()
  }

  def copy(
    message: String = this.message,
    _passthroughFields: immutable$Map[Short, TFieldBlob] = this._passthroughFields
  ): UserIdAlreadyCreated =
    new UserIdAlreadyCreated(
      message,
      _passthroughFields
    )

  override def canEqual(other: Any): Boolean = other.isInstanceOf[UserIdAlreadyCreated]

  private def _equals(x: UserIdAlreadyCreated, y: UserIdAlreadyCreated): Boolean =
      x.productArity == y.productArity &&
      x.productIterator.sameElements(y.productIterator)

  override def equals(other: Any): Boolean =
    canEqual(other) &&
      _equals(this, other.asInstanceOf[UserIdAlreadyCreated]) &&
      _passthroughFields == other.asInstanceOf[UserIdAlreadyCreated]._passthroughFields

  override def hashCode: Int = _root_.scala.runtime.ScalaRunTime._hashCode(this)

  override def toString: String = _root_.scala.runtime.ScalaRunTime._toString(this)

  override def getMessage: String = String.valueOf(message)

  override def productArity: Int = 1

  override def productElement(n: Int): Any = n match {
    case 0 => this.message
    case _ => throw new IndexOutOfBoundsException(n.toString)
  }

  override def productPrefix: String = "UserIdAlreadyCreated"

  def _codec: ThriftStructCodec3[UserIdAlreadyCreated] = UserIdAlreadyCreated
}

